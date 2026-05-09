package com.breeding.service;

import com.breeding.entity.BreedingProject;
import com.breeding.repository.BreedingProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 育种项目业务逻辑服务类
 * 处理育种项目的增删改查及相关业务操作
 */
@Service
@Transactional
public class BreedingProjectService {

    @Autowired
    private BreedingProjectRepository projectRepository;

    /**
     * 查询所有育种项目
     */
    public List<BreedingProject> findAll() {
        return projectRepository.findAll();
    }

    /**
     * 分页查询育种项目
     */
    public Page<BreedingProject> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    /**
     * 根据ID查询育种项目
     */
    public Optional<BreedingProject> findById(Long id) {
        return projectRepository.findById(id);
    }

    /**
     * 根据项目编号查询
     */
    public BreedingProject findByProjectCode(String projectCode) {
        return projectRepository.findByProjectCode(projectCode);
    }

    /**
     * 保存育种项目
     */
    public BreedingProject save(BreedingProject project) {
        return projectRepository.save(project);
    }

    /**
     * 更新育种项目
     */
    public BreedingProject update(Long id, BreedingProject projectDetails) {
        Optional<BreedingProject> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            BreedingProject project = optionalProject.get();
            project.setProjectCode(projectDetails.getProjectCode());
            project.setProjectName(projectDetails.getProjectName());
            project.setCropType(projectDetails.getCropType());
            project.setBreedingGoal(projectDetails.getBreedingGoal());
            project.setResponsiblePerson(projectDetails.getResponsiblePerson());
            project.setContactPhone(projectDetails.getContactPhone());
            project.setStartDate(projectDetails.getStartDate());
            project.setEndDate(projectDetails.getEndDate());
            project.setStatus(projectDetails.getStatus());
            project.setDescription(projectDetails.getDescription());
            return projectRepository.save(project);
        }
        return null;
    }

    /**
     * 删除育种项目
     */
    public boolean delete(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * 根据状态查询
     */
    public List<BreedingProject> findByStatus(String status) {
        return projectRepository.findByStatus(status);
    }

    /**
     * 模糊搜索
     */
    public List<BreedingProject> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return projectRepository.findAll();
        }
        return projectRepository.searchByKeyword(keyword);
    }

    /**
     * 获取进行中的项目数量
     */
    public Long getActiveProjectCount() {
        return projectRepository.countActiveProjects();
    }
}
