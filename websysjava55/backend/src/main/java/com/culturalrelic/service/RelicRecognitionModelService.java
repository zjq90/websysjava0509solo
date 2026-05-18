package com.culturalrelic.service;

import com.culturalrelic.entity.RelicRecognitionModel;
import com.culturalrelic.repository.RelicRecognitionModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 文物识别模型业务逻辑层
 */
@Service
public class RelicRecognitionModelService {

    @Autowired
    private RelicRecognitionModelRepository relicRecognitionModelRepository;

    public RelicRecognitionModel save(RelicRecognitionModel model) {
        return relicRecognitionModelRepository.save(model);
    }

    public Optional<RelicRecognitionModel> findById(Long id) {
        return relicRecognitionModelRepository.findById(id);
    }

    public Page<RelicRecognitionModel> findAll(Pageable pageable) {
        return relicRecognitionModelRepository.findAll(pageable);
    }

    public List<RelicRecognitionModel> findAll() {
        return relicRecognitionModelRepository.findAll();
    }

    public RelicRecognitionModel update(RelicRecognitionModel model) {
        return relicRecognitionModelRepository.save(model);
    }

    @Transactional
    public boolean delete(Long id) {
        int rows = relicRecognitionModelRepository.logicDelete(id);
        return rows > 0;
    }

    public RelicRecognitionModel findByModelNo(String modelNo) {
        return relicRecognitionModelRepository.findByModelNo(modelNo);
    }

    public RelicRecognitionModel findByIsDefault() {
        return relicRecognitionModelRepository.findByIsDefault(1);
    }

    public List<RelicRecognitionModel> findByStatus(Integer status) {
        return relicRecognitionModelRepository.findByStatus(status);
    }
}
