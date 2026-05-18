package com.secondhand.service;

import com.secondhand.entity.Dispute;
import com.secondhand.repository.DisputeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DisputeService {

    @Autowired
    private DisputeRepository disputeRepository;

    @Value("${application.upload.path:uploads}")
    private String uploadPath;

    @Value("${application.upload.max-file-size:5242880}")
    private Long maxFileSize;

    public Page<Dispute> findAll(String title, String type, String status, 
                                  Long complainantId, Long respondentId, Pageable pageable) {
        Specification<Dispute> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (title != null && !title.isEmpty()) {
                predicates.add(cb.like(root.get("title"), "%" + title + "%"));
            }
            if (type != null && !type.isEmpty()) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (complainantId != null) {
                predicates.add(cb.equal(root.get("complainantId"), complainantId));
            }
            if (respondentId != null) {
                predicates.add(cb.equal(root.get("respondentId"), respondentId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return disputeRepository.findAll(spec, pageable);
    }

    public Optional<Dispute> findById(Long id) {
        return disputeRepository.findById(id);
    }

    public Dispute findByOrderId(Long orderId) {
        return disputeRepository.findByOrderId(orderId);
    }

    @Transactional
    public Dispute save(Dispute dispute) {
        if (dispute.getId() == null) {
            dispute.setCreateTime(LocalDateTime.now());
            if (dispute.getStatus() == null) {
                dispute.setStatus("PENDING");
            }
        }
        dispute.setUpdateTime(LocalDateTime.now());
        return disputeRepository.save(dispute);
    }

    @Transactional
    public String uploadEvidence(MultipartFile file) throws IOException {
        if (file.getSize() > maxFileSize) {
            throw new IOException("文件大小超过限制");
        }
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + extension;
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path filePath = uploadDir.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath);
        return "/uploads/" + newFilename;
    }

    @Transactional
    public Dispute enterArbitrationResult(Long id, String arbitrationResult, Long arbitratorId) {
        Optional<Dispute> disputeOpt = disputeRepository.findById(id);
        if (disputeOpt.isPresent()) {
            Dispute dispute = disputeOpt.get();
            dispute.setArbitrationResult(arbitrationResult);
            dispute.setArbitratorId(arbitratorId);
            dispute.setStatus("RESOLVED");
            dispute.setProcessTime(LocalDateTime.now());
            dispute.setUpdateTime(LocalDateTime.now());
            return disputeRepository.save(dispute);
        }
        return null;
    }

}