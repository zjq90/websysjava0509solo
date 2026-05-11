package com.medical.appointment.service;

import com.medical.appointment.entity.Department;
import com.medical.appointment.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Map<String, Object>> getDepartmentTree() {
        List<Department> parentDepartments = departmentRepository.findByParentIsNullOrderBySortOrderAsc();
        return parentDepartments.stream().map(this::buildDepartmentTree).collect(Collectors.toList());
    }

    private Map<String, Object> buildDepartmentTree(Department dept) {
        Map<String, Object> result = convertToVO(dept);
        List<Department> children = departmentRepository.findByParent_IdOrderBySortOrderAsc(dept.getId());
        if (!children.isEmpty()) {
            result.put("children", children.stream().map(this::buildDepartmentTree).collect(Collectors.toList()));
        }
        return result;
    }

    public List<Map<String, Object>> searchDepartments(String keyword) {
        List<Department> departments = departmentRepository.searchByKeyword(keyword);
        return departments.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    public Department findById(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("科室不存在"));
    }

    public Map<String, Object> getDepartmentDetail(Long id) {
        Department dept = findById(id);
        Map<String, Object> result = convertToVO(dept);
        List<Department> children = departmentRepository.findByParent_IdOrderBySortOrderAsc(id);
        if (!children.isEmpty()) {
            result.put("children", children.stream().map(this::convertToVO).collect(Collectors.toList()));
        }
        return result;
    }

    private Map<String, Object> convertToVO(Department dept) {
        Map<String, Object> vo = new HashMap<>();
        vo.put("id", dept.getId());
        vo.put("name", dept.getName());
        vo.put("description", dept.getDescription());
        vo.put("icon", dept.getIcon());
        vo.put("sortOrder", dept.getSortOrder());
        vo.put("symptoms", dept.getSymptoms());
        vo.put("parentId", dept.getParent() != null ? dept.getParent().getId() : null);
        return vo;
    }
}
