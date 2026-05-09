package com.breeding.service;

import com.breeding.entity.FieldExperiment;
import com.breeding.repository.FieldExperimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 田间试验业务逻辑服务类
 */
@Service
@Transactional
public class FieldExperimentService {

    @Autowired
    private FieldExperimentRepository experimentRepository;

    public List<FieldExperiment> findAll() {
        return experimentRepository.findAll();
    }

    public Page<FieldExperiment> findAll(Pageable pageable) {
        return experimentRepository.findAll(pageable);
    }

    public Optional<FieldExperiment> findById(Long id) {
        return experimentRepository.findById(id);
    }

    public FieldExperiment findByExperimentCode(String code) {
        return experimentRepository.findByExperimentCode(code);
    }

    public FieldExperiment save(FieldExperiment experiment) {
        return experimentRepository.save(experiment);
    }

    public FieldExperiment update(Long id, FieldExperiment details) {
        Optional<FieldExperiment> optional = experimentRepository.findById(id);
        if (optional.isPresent()) {
            FieldExperiment experiment = optional.get();
            experiment.setExperimentCode(details.getExperimentCode());
            experiment.setExperimentName(details.getExperimentName());
            experiment.setLocation(details.getLocation());
            experiment.setYear(details.getYear());
            experiment.setSowingDate(details.getSowingDate());
            experiment.setHarvestDate(details.getHarvestDate());
            experiment.setGrowthPeriod(details.getGrowthPeriod());
            experiment.setPlantHeight(details.getPlantHeight());
            experiment.setEarLength(details.getEarLength());
            experiment.setGrainsPerEar(details.getGrainsPerEar());
            experiment.setThousandGrainWeight(details.getThousandGrainWeight());
            experiment.setPlotYield(details.getPlotYield());
            experiment.setYieldPerMu(details.getYieldPerMu());
            experiment.setPowderyMildewResistance(details.getPowderyMildewResistance());
            experiment.setRustResistance(details.getRustResistance());
            experiment.setSheathBlightResistance(details.getSheathBlightResistance());
            experiment.setDiseaseResistanceEvaluation(details.getDiseaseResistanceEvaluation());
            experiment.setLodgingResistance(details.getLodgingResistance());
            experiment.setDroughtResistance(details.getDroughtResistance());
            experiment.setColdResistance(details.getColdResistance());
            experiment.setProteinContent(details.getProteinContent());
            experiment.setStarchContent(details.getStarchContent());
            experiment.setFatContent(details.getFatContent());
            experiment.setOverallEvaluation(details.getOverallEvaluation());
            experiment.setStatus(details.getStatus());
            experiment.setRemarks(details.getRemarks());
            return experimentRepository.save(experiment);
        }
        return null;
    }

    public boolean delete(Long id) {
        if (experimentRepository.existsById(id)) {
            experimentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<FieldExperiment> findByProjectId(Long projectId) {
        return experimentRepository.findByProjectId(projectId);
    }

    public List<FieldExperiment> findByYear(Integer year) {
        return experimentRepository.findByYear(year);
    }

    public List<FieldExperiment> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return experimentRepository.findAll();
        }
        return experimentRepository.searchByKeyword(keyword);
    }

    public Double getAverageYieldByLocation(String location) {
        return experimentRepository.getAverageYieldByLocation(location);
    }
}
