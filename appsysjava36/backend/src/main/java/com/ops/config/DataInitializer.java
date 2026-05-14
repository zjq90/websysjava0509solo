package com.ops.config;

import com.ops.entity.Technician;
import com.ops.entity.User;
import com.ops.entity.WorkOrder;
import com.ops.repository.TechnicianRepository;
import com.ops.repository.UserRepository;
import com.ops.repository.WorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 数据初始化类
 * 系统启动时自动生成测试数据
 * 
 * @author ops-admin
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TechnicianRepository technicianRepository;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Override
    public void run(String... args) throws Exception {
        initUsers();
        initTechnicians();
        initWorkOrders();
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            String[] names = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十"};
            String[] packages = {"BASIC", "STANDARD", "PREMIUM"};
            
            for (int i = 0; i < names.length; i++) {
                User user = new User();
                user.setUsername("user" + (i + 1));
                user.setPassword("123456");
                user.setRealName(names[i]);
                user.setPhone("1380000" + String.format("%04d", i + 1));
                user.setEmail("user" + (i + 1) + "@example.com");
                user.setAddress("北京市朝阳区XX街道XX号" + (i + 1) + "室");
                user.setLatitude(39.9042 + Math.random() * 0.1);
                user.setLongitude(116.4074 + Math.random() * 0.1);
                user.setPackageType(packages[i % 3]);
                user.setStatus("ACTIVE");
                user.setIsActive(true);
                user.setLastLoginTime(LocalDateTime.now().minusDays((long) (Math.random() * 30)));
                user.setExpireDate(LocalDateTime.now().plusDays((long) (Math.random() * 180)));
                userRepository.save(user);
            }
        }
    }

    private void initTechnicians() {
        if (technicianRepository.count() == 0) {
            String[] names = {"王师傅", "李师傅", "张师傅", "刘师傅", "陈师傅"};
            String[] areas = {"朝阳区", "海淀区", "丰台区", "东城区", "西城区"};
            
            for (int i = 0; i < names.length; i++) {
                Technician tech = new Technician();
                tech.setName(names[i]);
                tech.setPhone("1390000" + String.format("%04d", i + 1));
                tech.setWorkArea(areas[i]);
                tech.setLatitude(39.9042 + Math.random() * 0.1);
                tech.setLongitude(116.4074 + Math.random() * 0.1);
                tech.setCurrentLoad(0);
                tech.setMaxLoad(10);
                tech.setStatus("ONLINE");
                tech.setRating(4.5 + Math.random() * 0.5);
                tech.setTotalOrders((int) (Math.random() * 100));
                technicianRepository.save(tech);
            }
        }
    }

    private void initWorkOrders() {
        if (workOrderRepository.count() == 0) {
            String[] types = {"INSTALL", "REPAIR", "MAINTENANCE", "UPGRADE"};
            String[] priorities = {"LOW", "MEDIUM", "HIGH"};
            String[] statuses = {"PENDING", "ASSIGNED", "IN_PROGRESS", "COMPLETED"};
            
            java.util.List<User> users = userRepository.findAll();
            java.util.List<Technician> techs = technicianRepository.findAll();
            
            for (int i = 0; i < 20; i++) {
                WorkOrder order = new WorkOrder();
                order.setOrderNo("WO202401" + String.format("%04d", i + 1));
                order.setOrderType(types[i % 4]);
                order.setPriority(priorities[i % 3]);
                order.setStatus(statuses[i % 4]);
                
                User user = users.get(i % users.size());
                order.setUserId(user.getId());
                order.setUserName(user.getRealName());
                order.setUserPhone(user.getPhone());
                order.setUserAddress(user.getAddress());
                order.setLatitude(user.getLatitude());
                order.setLongitude(user.getLongitude());
                
                if (!"PENDING".equals(order.getStatus())) {
                    Technician tech = techs.get(i % techs.size());
                    order.setTechnicianId(tech.getId());
                    order.setTechnicianName(tech.getName());
                    order.setTechnicianPhone(tech.getPhone());
                    order.setAssignTime(LocalDateTime.now().minusHours((long) (Math.random() * 24 + 1)));
                    
                    if ("IN_PROGRESS".equals(order.getStatus()) || "COMPLETED".equals(order.getStatus())) {
                        order.setAcceptTime(order.getAssignTime().plusHours((long) (Math.random() * 2)));
                        order.setResponseDuration((long) (Math.random() * 60 + 10));
                    }
                    
                    if ("COMPLETED".equals(order.getStatus())) {
                        order.setStartTime(order.getAcceptTime().plusHours((long) (Math.random() * 4)));
                        order.setCompleteTime(order.getStartTime().plusHours((long) (Math.random() * 2 + 1)));
                        order.setHandleDuration((long) (Math.random() * 60 + 30));
                        order.setIsFirstFix(Math.random() > 0.2);
                        order.setSatisfactionScore((int) (Math.random() * 2 + 3));
                        order.setEvaluation("服务很好，师傅很专业！");
                    }
                }
                
                order.setDescription("这是一个测试工单，需要进行" + order.getOrderType() + "服务");
                order.setCreateTime(LocalDateTime.now().minusDays((long) (Math.random() * 7)));
                order.setUpdateTime(LocalDateTime.now());
                
                workOrderRepository.save(order);
            }
        }
    }
}
