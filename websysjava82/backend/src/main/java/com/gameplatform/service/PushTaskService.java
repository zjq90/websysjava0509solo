package com.gameplatform.service;

import com.gameplatform.entity.Message;
import com.gameplatform.entity.PushTask;
import com.gameplatform.entity.User;
import com.gameplatform.repository.MessageRepository;
import com.gameplatform.repository.PushTaskRepository;
import com.gameplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PushTaskService {
    @Autowired
    private PushTaskRepository pushTaskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    public List<PushTask> findAll() {
        return pushTaskRepository.findAll();
    }

    public List<PushTask> findByStatus(String status) {
        return pushTaskRepository.findByStatus(status);
    }

    public Optional<PushTask> findById(Long id) {
        return pushTaskRepository.findById(id);
    }

    public PushTask save(PushTask task) {
        if (task.getStatus() == null) {
            task.setStatus("DRAFT");
        }
        return pushTaskRepository.save(task);
    }

    public void deleteById(Long id) {
        pushTaskRepository.deleteById(id);
    }

    public PushTask update(Long id, PushTask task) {
        task.setId(id);
        return pushTaskRepository.save(task);
    }

    @Transactional
    public PushTask sendTask(Long taskId) {
        Optional<PushTask> opt = pushTaskRepository.findById(taskId);
        if (!opt.isPresent()) {
            return null;
        }

        PushTask task = opt.get();
        if ("SENT".equals(task.getStatus()) || "SENDING".equals(task.getStatus())) {
            return task;
        }

        List<User> targetUsers = getTargetUsers(task);
        task.setTotalUsers(targetUsers.size());
        task.setStatus("SENDING");
        task.setSentTime(LocalDateTime.now());
        pushTaskRepository.save(task);

        int deliveredCount = 0;
        for (User user : targetUsers) {
            Message message = new Message();
            message.setTaskId(task.getId());
            message.setUserId(user.getId());
            message.setTitle(task.getTitle());
            message.setContent(task.getContent());
            message.setSentTime(LocalDateTime.now());
            messageRepository.save(message);
            deliveredCount++;
        }

        task.setDeliveredCount(deliveredCount);
        task.setStatus("SENT");
        return pushTaskRepository.save(task);
    }

    private List<User> getTargetUsers(PushTask task) {
        String targetType = task.getTargetType();
        Integer memberLevel = task.getMemberLevel();
        Integer activeDays = task.getActiveDays();

        if ("ALL".equals(targetType)) {
            return userRepository.findByEnabled(true);
        } else if ("MEMBER_LEVEL".equals(targetType) && memberLevel != null) {
            if (activeDays != null) {
                LocalDateTime activeSince = LocalDateTime.now().minusDays(activeDays);
                return userRepository.findByMemberLevelAndLastActiveTime(memberLevel, activeSince);
            }
            return userRepository.findByMemberLevelGreaterThanEqual(memberLevel);
        } else if ("ACTIVE".equals(targetType) && activeDays != null) {
            LocalDateTime activeSince = LocalDateTime.now().minusDays(activeDays);
            return userRepository.findByLastActiveTimeAfter(activeSince);
        }

        return new ArrayList<>();
    }

    @Scheduled(fixedRate = 60000)
    public void processScheduledTasks() {
        List<PushTask> scheduledTasks = pushTaskRepository.findScheduledTasksToSend(LocalDateTime.now());
        for (PushTask task : scheduledTasks) {
            sendTask(task.getId());
        }
    }

    public void updateOpenedCount(Long taskId) {
        Optional<PushTask> opt = pushTaskRepository.findById(taskId);
        if (opt.isPresent()) {
            PushTask task = opt.get();
            long openedCount = messageRepository.countByTaskIdAndRead(taskId, true);
            task.setOpenedCount((int) openedCount);
            pushTaskRepository.save(task);
        }
    }
}
