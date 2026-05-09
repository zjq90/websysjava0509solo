package com.breeding.service;

import com.breeding.entity.WeatherData;
import com.breeding.repository.WeatherDataRepository;
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
 * 气象数据业务逻辑服务类
 */
@Service
@Transactional
public class WeatherDataService {

    @Autowired
    private WeatherDataRepository weatherRepository;

    public List<WeatherData> findAll() {
        return weatherRepository.findAll();
    }

    public Page<WeatherData> findAll(Pageable pageable) {
        return weatherRepository.findAll(pageable);
    }

    public Optional<WeatherData> findById(Long id) {
        return weatherRepository.findById(id);
    }

    public WeatherData save(WeatherData weatherData) {
        return weatherRepository.save(weatherData);
    }

    public WeatherData update(Long id, WeatherData details) {
        Optional<WeatherData> optional = weatherRepository.findById(id);
        if (optional.isPresent()) {
            WeatherData data = optional.get();
            data.setRecordDate(details.getRecordDate());
            data.setLocation(details.getLocation());
            data.setAvgTemperature(details.getAvgTemperature());
            data.setMaxTemperature(details.getMaxTemperature());
            data.setMinTemperature(details.getMinTemperature());
            data.setAvgHumidity(details.getAvgHumidity());
            data.setRainfall(details.getRainfall());
            data.setSunshineHours(details.getSunshineHours());
            data.setAvgWindSpeed(details.getAvgWindSpeed());
            data.setMaxWindSpeed(details.getMaxWindSpeed());
            data.setWindDirection(details.getWindDirection());
            data.setAirPressure(details.getAirPressure());
            data.setEvaporation(details.getEvaporation());
            data.setWeatherDescription(details.getWeatherDescription());
            data.setRemarks(details.getRemarks());
            data.setDataSource(details.getDataSource());
            return weatherRepository.save(data);
        }
        return null;
    }

    public boolean delete(Long id) {
        if (weatherRepository.existsById(id)) {
            weatherRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<WeatherData> findByLocation(String location) {
        return weatherRepository.findByLocation(location);
    }

    public List<WeatherData> findByLocationAndDateRange(String location, LocalDate startDate, LocalDate endDate) {
        return weatherRepository.findByLocationAndRecordDateBetween(location, startDate, endDate);
    }

    public Map<String, Object> analyzeWeatherImpact(String location, LocalDate startDate, LocalDate endDate) {
        Map<String, Object> analysis = new HashMap<>();
        analysis.put("location", location);
        analysis.put("startDate", startDate);
        analysis.put("endDate", endDate);
        analysis.put("averageTemperature", weatherRepository.getAverageTemperature(location, startDate, endDate));
        analysis.put("totalRainfall", weatherRepository.getTotalRainfall(location, startDate, endDate));
        analysis.put("averageSunshineHours", weatherRepository.getAverageSunshineHours(location, startDate, endDate));
        return analysis;
    }
}
