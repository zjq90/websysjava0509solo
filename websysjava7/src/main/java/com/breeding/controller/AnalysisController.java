package com.breeding.controller;

import com.breeding.entity.FieldExperiment;
import com.breeding.entity.SoilData;
import com.breeding.entity.WeatherData;
import com.breeding.service.FieldExperimentService;
import com.breeding.service.SoilDataService;
import com.breeding.service.WeatherDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 环境因素分析控制器
 * 用于分析气象和土壤数据对育种的影响
 */
@Controller
@RequestMapping("/analysis")
public class AnalysisController {

    @Autowired
    private WeatherDataService weatherService;

    @Autowired
    private SoilDataService soilService;

    @Autowired
    private FieldExperimentService experimentService;

    /**
     * 环境分析页面
     */
    @GetMapping
    public String analysisPage(Model model) {
        List<FieldExperiment> experiments = experimentService.findAll();
        List<String> locations = new ArrayList<>();
        for (FieldExperiment exp : experiments) {
            if (!locations.contains(exp.getLocation())) {
                locations.add(exp.getLocation());
            }
        }
        model.addAttribute("locations", locations);
        model.addAttribute("experiments", experiments);
        return "analysis/index";
    }

    /**
     * 获取环境影响分析数据
     */
    @GetMapping("/data")
    @ResponseBody
    public Map<String, Object> getAnalysisData(@RequestParam String location) {
        Map<String, Object> result = new HashMap<>();

        List<WeatherData> weatherList = weatherService.findByLocation(location);
        List<SoilData> soilList = soilService.findByLocation(location);
        List<FieldExperiment> experiments = experimentService.findAll();

        List<FieldExperiment> locationExperiments = new ArrayList<>();
        for (FieldExperiment exp : experiments) {
            if (exp.getLocation().equals(location) && exp.getStatus().equals("已完成")) {
                locationExperiments.add(exp);
            }
        }

        double avgYield = 0;
        if (!locationExperiments.isEmpty()) {
            double total = 0;
            for (FieldExperiment exp : locationExperiments) {
                if (exp.getYieldPerMu() != null) {
                    total += exp.getYieldPerMu();
                }
            }
            avgYield = total / locationExperiments.size();
        }

        double avgTemp = 0;
        double totalRainfall = 0;
        double avgSunshine = 0;
        if (!weatherList.isEmpty()) {
            double tempTotal = 0;
            double rainTotal = 0;
            double sunTotal = 0;
            int tempCount = 0;
            int rainCount = 0;
            int sunCount = 0;
            for (WeatherData w : weatherList) {
                if (w.getAvgTemperature() != null) {
                    tempTotal += w.getAvgTemperature();
                    tempCount++;
                }
                if (w.getRainfall() != null) {
                    rainTotal += w.getRainfall();
                    rainCount++;
                }
                if (w.getSunshineHours() != null) {
                    sunTotal += w.getSunshineHours();
                    sunCount++;
                }
            }
            avgTemp = tempCount > 0 ? tempTotal / tempCount : 0;
            totalRainfall = rainTotal;
            avgSunshine = sunCount > 0 ? sunTotal / sunCount : 0;
        }

        double avgPh = 0;
        double avgOrganicMatter = 0;
        if (!soilList.isEmpty()) {
            double phTotal = 0;
            double omTotal = 0;
            int phCount = 0;
            int omCount = 0;
            for (SoilData s : soilList) {
                if (s.getPhValue() != null) {
                    phTotal += s.getPhValue();
                    phCount++;
                }
                if (s.getOrganicMatter() != null) {
                    omTotal += s.getOrganicMatter();
                    omCount++;
                }
            }
            avgPh = phCount > 0 ? phTotal / phCount : 0;
            avgOrganicMatter = omCount > 0 ? omTotal / omCount : 0;
        }

        result.put("location", location);
        result.put("averageYield", avgYield);
        result.put("experimentCount", locationExperiments.size());

        Map<String, Object> weatherMap = new HashMap<>();
        weatherMap.put("averageTemperature", avgTemp);
        weatherMap.put("totalRainfall", totalRainfall);
        weatherMap.put("averageSunshine", avgSunshine);
        weatherMap.put("recordCount", weatherList.size());
        result.put("weather", weatherMap);

        Map<String, Object> soilMap = new HashMap<>();
        soilMap.put("averagePh", avgPh);
        soilMap.put("averageOrganicMatter", avgOrganicMatter);
        soilMap.put("recordCount", soilList.size());
        result.put("soil", soilMap);

        result.put("analysis", generateAnalysis(avgTemp, totalRainfall, avgSunshine, avgPh, avgOrganicMatter, avgYield));

        return result;
    }

    private List<String> generateAnalysis(double avgTemp, double totalRain, double avgSun, double avgPh, double avgOM, double avgYield) {
        List<String> analysis = new ArrayList<>();

        if (avgTemp > 0) {
            if (avgTemp < 15) {
                analysis.add("该地区平均气温偏低（" + String.format("%.1f", avgTemp) + "℃），可能影响作物生长速度，建议选择耐寒品种。");
            } else if (avgTemp > 28) {
                analysis.add("该地区平均气温偏高（" + String.format("%.1f", avgTemp) + "℃），需注意高温热害，建议选择耐热品种。");
            } else {
                analysis.add("该地区平均气温适宜（" + String.format("%.1f", avgTemp) + "℃），有利于大多数作物生长。");
            }
        }

        if (totalRain > 0) {
            if (totalRain < 200) {
                analysis.add("该地区降雨量偏少（累计" + String.format("%.1f", totalRain) + "mm），需注意抗旱保苗，建议选择抗旱品种。");
            } else if (totalRain > 800) {
                analysis.add("该地区降雨量充沛（累计" + String.format("%.1f", totalRain) + "mm），需注意排水防涝，建议选择耐湿品种。");
            } else {
                analysis.add("该地区降雨量适中（累计" + String.format("%.1f", totalRain) + "mm），有利于作物生长发育。");
            }
        }

        if (avgSun > 0) {
            if (avgSun < 4) {
                analysis.add("该地区日照时数偏少（日均" + String.format("%.1f", avgSun) + "小时），需注意通风透光，建议选择耐阴品种。");
            } else if (avgSun > 8) {
                analysis.add("该地区日照充足（日均" + String.format("%.1f", avgSun) + "小时），有利于光合作用，适合种植喜光作物。");
            }
        }

        if (avgPh > 0) {
            if (avgPh < 5.5) {
                analysis.add("土壤偏酸（pH " + String.format("%.1f", avgPh) + "），建议适当施用石灰改良，选择耐酸品种。");
            } else if (avgPh > 8.5) {
                analysis.add("土壤偏碱（pH " + String.format("%.1f", avgPh) + "），建议施用有机肥改良，选择耐碱品种。");
            } else {
                analysis.add("土壤酸碱度适宜（pH " + String.format("%.1f", avgPh) + "），有利于大多数作物生长。");
            }
        }

        if (avgOM > 0) {
            if (avgOM < 10) {
                analysis.add("土壤有机质含量偏低（" + String.format("%.1f", avgOM) + "g/kg），建议增施有机肥提高土壤肥力。");
            } else if (avgOM > 30) {
                analysis.add("土壤有机质含量丰富（" + String.format("%.1f", avgOM) + "g/kg），土壤肥力水平较高。");
            }
        }

        if (avgYield > 0) {
            analysis.add("该地区平均亩产为" + String.format("%.1f", avgYield) + "公斤，可作为品种选育的参考基准。");
        }

        if (analysis.isEmpty()) {
            analysis.add("暂无足够数据进行环境因素分析，请先录入气象和土壤数据。");
        }

        return analysis;
    }
}
