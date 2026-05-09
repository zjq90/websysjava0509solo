package com.breeding.service;

import com.breeding.entity.SoilData;
import com.breeding.repository.SoilDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 土壤数据业务逻辑服务类
 */
@Service
@Transactional
public class SoilDataService {

    @Autowired
    private SoilDataRepository soilRepository;

    public List<SoilData> findAll() {
        return soilRepository.findAll();
    }

    public Page<SoilData> findAll(Pageable pageable) {
        return soilRepository.findAll(pageable);
    }

    public Optional<SoilData> findById(Long id) {
        return soilRepository.findById(id);
    }

    public SoilData save(SoilData soilData) {
        return soilRepository.save(soilData);
    }

    public SoilData update(Long id, SoilData details) {
        Optional<SoilData> optional = soilRepository.findById(id);
        if (optional.isPresent()) {
            SoilData data = optional.get();
            data.setRecordDate(details.getRecordDate());
            data.setLocation(details.getLocation());
            data.setSoilType(details.getSoilType());
            data.setSoilTexture(details.getSoilTexture());
            data.setPhValue(details.getPhValue());
            data.setOrganicMatter(details.getOrganicMatter());
            data.setTotalNitrogen(details.getTotalNitrogen());
            data.setAvailablePhosphorus(details.getAvailablePhosphorus());
            data.setAvailablePotassium(details.getAvailablePotassium());
            data.setTotalPhosphorus(details.getTotalPhosphorus());
            data.setTotalPotassium(details.getTotalPotassium());
            data.setAlkalineHydrolyzableN(details.getAlkalineHydrolyzableN());
            data.setExchangeableCalcium(details.getExchangeableCalcium());
            data.setExchangeableMagnesium(details.getExchangeableMagnesium());
            data.setAvailableSulfur(details.getAvailableSulfur());
            data.setAvailableZinc(details.getAvailableZinc());
            data.setAvailableBoron(details.getAvailableBoron());
            data.setAvailableManganese(details.getAvailableManganese());
            data.setAvailableCopper(details.getAvailableCopper());
            data.setAvailableIron(details.getAvailableIron());
            data.setCationExchangeCapacity(details.getCationExchangeCapacity());
            data.setBulkDensity(details.getBulkDensity());
            data.setPorosity(details.getPorosity());
            data.setMoistureContent(details.getMoistureContent());
            data.setElectricalConductivity(details.getElectricalConductivity());
            data.setSaltContent(details.getSaltContent());
            data.setOverallEvaluation(details.getOverallEvaluation());
            data.setImprovementSuggestion(details.getImprovementSuggestion());
            data.setRemarks(details.getRemarks());
            data.setDataSource(details.getDataSource());
            return soilRepository.save(data);
        }
        return null;
    }

    public boolean delete(Long id) {
        if (soilRepository.existsById(id)) {
            soilRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<SoilData> findByLocation(String location) {
        return soilRepository.findByLocation(location);
    }

    public Map<String, Object> analyzeSoilQuality(String location) {
        Map<String, Object> analysis = new HashMap<>();
        analysis.put("location", location);
        analysis.put("averagePhValue", soilRepository.getAveragePhValue(location));
        analysis.put("averageOrganicMatter", soilRepository.getAverageOrganicMatter(location));
        return analysis;
    }
}
