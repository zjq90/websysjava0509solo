package com.breeding.service;

import com.breeding.entity.CrossCombination;
import com.breeding.repository.CrossCombinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 杂交组合业务逻辑服务类
 */
@Service
@Transactional
public class CrossCombinationService {

    @Autowired
    private CrossCombinationRepository combinationRepository;

    public List<CrossCombination> findAll() {
        return combinationRepository.findAll();
    }

    public Page<CrossCombination> findAll(Pageable pageable) {
        return combinationRepository.findAll(pageable);
    }

    public Optional<CrossCombination> findById(Long id) {
        return combinationRepository.findById(id);
    }

    public CrossCombination findByCombinationCode(String code) {
        return combinationRepository.findByCombinationCode(code);
    }

    public CrossCombination save(CrossCombination combination) {
        return combinationRepository.save(combination);
    }

    public CrossCombination update(Long id, CrossCombination details) {
        Optional<CrossCombination> optional = combinationRepository.findById(id);
        if (optional.isPresent()) {
            CrossCombination combination = optional.get();
            combination.setCombinationCode(details.getCombinationCode());
            combination.setCombinationName(details.getCombinationName());
            combination.setCrossDate(details.getCrossDate());
            combination.setCrossLocation(details.getCrossLocation());
            combination.setCrossMethod(details.getCrossMethod());
            combination.setCrossPurpose(details.getCrossPurpose());
            combination.setFlowerCount(details.getFlowerCount());
            combination.setSeedSetRate(details.getSeedSetRate());
            combination.setSeedCount(details.getSeedCount());
            combination.setGeneration(details.getGeneration());
            combination.setStatus(details.getStatus());
            combination.setOffspringTraits(details.getOffspringTraits());
            combination.setEvaluation(details.getEvaluation());
            combination.setRemarks(details.getRemarks());
            return combinationRepository.save(combination);
        }
        return null;
    }

    public boolean delete(Long id) {
        if (combinationRepository.existsById(id)) {
            combinationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<CrossCombination> findByProjectId(Long projectId) {
        return combinationRepository.findByProjectId(projectId);
    }

    public List<CrossCombination> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return combinationRepository.findAll();
        }
        return combinationRepository.searchByKeyword(keyword);
    }
}
