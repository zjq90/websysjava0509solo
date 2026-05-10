package com.websys.service;

import com.websys.entity.Permission;
import com.websys.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public Page<Permission> findPage(String name, String code, Integer status, Pageable pageable) {
        return permissionRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            if (code != null && !code.isEmpty()) {
                predicates.add(cb.like(root.get("code"), "%" + code + "%"));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id);
    }

    public Optional<Permission> findByCode(String code) {
        return permissionRepository.findByCode(code);
    }

    @Transactional
    public Permission save(Permission permission) {
        if (permission.getId() != null) {
            permission.setUpdateTime(LocalDateTime.now());
        }
        return permissionRepository.save(permission);
    }

    @Transactional
    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }

    public boolean existsByCode(String code) {
        return permissionRepository.existsByCode(code);
    }
}
