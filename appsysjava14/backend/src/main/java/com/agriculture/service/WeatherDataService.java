package com.agriculture.service;

import com.agriculture.entity.WeatherData;
import com.agriculture.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * 气象数据服务类
 * 对接气象API，实时获取并记录种植区域环境数据
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Service
@Transactional
public class WeatherDataService {

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    private static final Random RANDOM = new Random();

    /**
     * 保存气象数据
     * 
     * @param weatherData 气象数据
     * @return 保存后的数据
     */
    public WeatherData save(WeatherData weatherData) {
        return weatherDataRepository.save(weatherData);
    }

    /**
     * 批量保存气象数据
     * 
     * @param dataList 数据列表
     * @return 保存后的列表
     */
    public List<WeatherData> saveAll(List<WeatherData> dataList) {
        return weatherDataRepository.saveAll(dataList);
    }

    /**
     * 根据ID删除数据
     * 
     * @param id 数据ID
     */
    public void deleteById(Long id) {
        weatherDataRepository.deleteById(id);
    }

    /**
     * 根据ID查找数据
     * 
     * @param id 数据ID
     * @return 气象数据
     */
    public Optional<WeatherData> findById(Long id) {
        return weatherDataRepository.findById(id);
    }

    /**
     * 查询所有数据
     * 
     * @return 数据列表
     */
    public List<WeatherData> findAll() {
        return weatherDataRepository.findAll();
    }

    /**
     * 根据地块ID查询气象数据
     * 
     * @param plotId 地块ID
     * @return 数据列表
     */
    public List<WeatherData> findByPlotId(Long plotId) {
        return weatherDataRepository.findByPlotIdOrderByRecordDateDesc(plotId);
    }

    /**
     * 根据城市编码查询
     * 
     * @param cityCode 城市编码
     * @return 数据列表
     */
    public List<WeatherData> findByCityCode(String cityCode) {
        return weatherDataRepository.findByCityCodeOrderByRecordDateDesc(cityCode);
    }

    /**
     * 查询地块在时间范围内的气象数据
     * 
     * @param plotId 地块ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 数据列表
     */
    public List<WeatherData> findByPlotIdAndDateRange(Long plotId, LocalDateTime startTime, LocalDateTime endTime) {
        return weatherDataRepository.findByPlotIdAndDateRange(plotId, startTime, endTime);
    }

    /**
     * 模拟从气象API获取数据
     * 实际项目中应对接真实的气象API服务
     * 
     * @param cityCode 城市编码
     * @param plotId 地块ID
     * @return 气象数据
     */
    public WeatherData fetchFromApi(String cityCode, Long plotId) {
        WeatherData weatherData = new WeatherData();
        weatherData.setPlotId(plotId);
        weatherData.setCityCode(cityCode);
        weatherData.setRecordDate(LocalDateTime.now());
        weatherData.setDataSource("API");

        String[] weathers = {"SUNNY", "PARTLY_CLOUDY", "CLOUDY", "RAINY"};
        String[] windDirections = {"N", "S", "E", "W", "NE", "SE", "NW", "SW"};

        double temp = 15 + RANDOM.nextDouble() * 20;
        weatherData.setWeatherCondition(weathers[RANDOM.nextInt(weathers.length)]);
        weatherData.setTemperature(BigDecimal.valueOf(temp));
        weatherData.setFeelsLike(BigDecimal.valueOf(temp - 2 + RANDOM.nextDouble() * 4));
        weatherData.setTempMin(BigDecimal.valueOf(10 + RANDOM.nextDouble() * 10));
        weatherData.setTempMax(BigDecimal.valueOf(25 + RANDOM.nextDouble() * 10));
        weatherData.setHumidity(BigDecimal.valueOf(40 + RANDOM.nextDouble() * 40));
        weatherData.setWindDirection(windDirections[RANDOM.nextInt(windDirections.length)]);
        weatherData.setWindSpeed(BigDecimal.valueOf(1 + RANDOM.nextDouble() * 8));
        weatherData.setCloudCover(BigDecimal.valueOf(RANDOM.nextDouble() * 100));
        weatherData.setPrecipitation(BigDecimal.valueOf(RANDOM.nextDouble() * 50));
        weatherData.setUvIndex(RANDOM.nextInt(5) == 0 ? "弱" : RANDOM.nextInt(3) == 0 ? "中等" : "强");
        weatherData.setAqi(30 + RANDOM.nextInt(120));

        return weatherDataRepository.save(weatherData);
    }

    /**
     * 查询城市最新气象数据
     * 
     * @param cityCode 城市编码
     * @return 最新气象数据
     */
    public WeatherData getLatestWeather(String cityCode) {
        List<WeatherData> list = weatherDataRepository.findLatestByCityCode(cityCode);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
}
