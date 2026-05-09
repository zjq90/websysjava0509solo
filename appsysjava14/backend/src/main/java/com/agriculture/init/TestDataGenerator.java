package com.agriculture.init;

import com.agriculture.entity.*;
import com.agriculture.repository.*;
import com.agriculture.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 测试数据生成器
 * 系统启动时自动生成测试数据，辅助功能测试
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Component
public class TestDataGenerator implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlotRepository plotRepository;
    @Autowired
    private CropRepository cropRepository;
    @Autowired
    private FieldRecordRepository fieldRecordRepository;
    @Autowired
    private PestImageRepository pestImageRepository;
    @Autowired
    private ObservationTemplateRepository templateRepository;
    @Autowired
    private WeatherDataRepository weatherDataRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EncryptionUtil encryptionUtil;

    private static final Random RANDOM = new Random();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("========================================");
        System.out.println("  正在生成测试数据...");
        System.out.println("========================================");

        generateUsers();
        generatePlots();
        generateCrops();
        generateTemplates();
        generateFieldRecords();
        generatePestImages();
        generateWeatherData();
        generateCustomers();

        System.out.println("========================================");
        System.out.println("  测试数据生成完成！");
        System.out.println("  管理员账号: admin / 123456");
        System.out.println("  普通用户账号: user01 / 123456");
        System.out.println("========================================");
    }

    /**
     * 生成用户测试数据
     */
    private void generateUsers() {
        if (userRepository.count() > 0) {
            System.out.println("用户数据已存在，跳过生成");
            return;
        }

        List<User> users = new ArrayList<>();

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(encryptionUtil.hashPassword("123456"));
        admin.setRealName("系统管理员");
        admin.setRole("ADMIN");
        admin.setPhone(encryptionUtil.encrypt("13800138000"));
        admin.setEmail("admin@agriculture.com");
        admin.setDepartment("研发部");
        admin.setStatus("ACTIVE");
        users.add(admin);

        String[] userNames = {"张三", "李四", "王五", "赵六", "陈七"};
        for (int i = 0; i < userNames.length; i++) {
            User user = new User();
            user.setUsername("user0" + (i + 1));
            user.setPassword(encryptionUtil.hashPassword("123456"));
            user.setRealName(userNames[i]);
            user.setRole("USER");
            user.setPhone(encryptionUtil.encrypt("1390000000" + (i + 1)));
            user.setEmail("user0" + (i + 1) + "@agriculture.com");
            user.setDepartment("田间调查组" + ((i % 3) + 1));
            user.setStatus("ACTIVE");
            users.add(user);
        }

        userRepository.saveAll(users);
        System.out.println("生成用户数据: " + users.size() + " 条");
    }

    /**
     * 生成地块测试数据
     */
    private void generatePlots() {
        if (plotRepository.count() > 0) {
            System.out.println("地块数据已存在，跳过生成");
            return;
        }

        List<Plot> plots = new ArrayList<>();
        
        String[][] plotInfo = {
            {"DK001", "河南郑州试验基地A区", "河南省", "郑州市", "金水区", "黄河东路100号"},
            {"DK002", "河南新乡试验基地", "河南省", "新乡市", "原阳县", "农业路200号"},
            {"DK003", "山东济南试验场", "山东省", "济南市", "历城区", "工业北路300号"},
            {"DK004", "河北石家庄示范田", "河北省", "石家庄市", "正定县", "新区大道400号"},
            {"DK005", "江苏南京试验基地", "江苏省", "南京市", "浦口区", "浦珠路500号"}
        };

        for (int i = 0; i < plotInfo.length; i++) {
            Plot plot = new Plot();
            plot.setPlotCode(plotInfo[i][0]);
            plot.setPlotName(plotInfo[i][1]);
            plot.setProvince(plotInfo[i][2]);
            plot.setCity(plotInfo[i][3]);
            plot.setDistrict(plotInfo[i][4]);
            plot.setAddress(plotInfo[i][5]);
            plot.setLatitude(BigDecimal.valueOf(30 + RANDOM.nextDouble() * 10));
            plot.setLongitude(BigDecimal.valueOf(113 + RANDOM.nextDouble() * 10));
            plot.setArea(BigDecimal.valueOf(10 + RANDOM.nextDouble() * 50));
            plot.setSoilType(new String[]{"壤土", "粘土", "砂土"}[RANDOM.nextInt(3)]);
            plot.setIrrigationCondition(new String[]{"GOOD", "MEDIUM", "POOR"}[RANDOM.nextInt(3)]);
            plot.setResponsiblePerson("调查组" + ((i % 3) + 1));
            plot.setDescription("这是编号为" + plotInfo[i][0] + "的试验地块");
            plot.setStatus("ACTIVE");
            plot.setCreatedBy(1L);
            plots.add(plot);
        }

        plotRepository.saveAll(plots);
        System.out.println("生成地块数据: " + plots.size() + " 条");
    }

    /**
     * 生成作物品种测试数据
     */
    private void generateCrops() {
        if (cropRepository.count() > 0) {
            System.out.println("作物数据已存在，跳过生成");
            return;
        }

        List<Crop> crops = new ArrayList<>();

        String[][] cropInfo = {
            {"CORN", "郑单958", "ZD958", "河南省农科院"},
            {"CORN", "先玉335", "XY335", "美国先锋公司"},
            {"CORN", "登海605", "DH605", "山东登海种业"},
            {"WHEAT", "济麦22", "JM22", "山东省农科院"},
            {"WHEAT", "山农28", "SN28", "山东农业大学"},
            {"WHEAT", "周麦27", "ZM27", "河南省周口市农科院"},
            {"RICE", "两优培九", "LYPJ", "国家杂交水稻中心"},
            {"RICE", "深两优5814", "SLY5814", "湖南隆平高科"}
        };

        for (int i = 0; i < cropInfo.length; i++) {
            Crop crop = new Crop();
            crop.setBatchCode(generateBatchCode(i + 1));
            crop.setCropType(cropInfo[i][0]);
            crop.setCropName(cropInfo[i][1]);
            crop.setVarietyCode(cropInfo[i][2]);
            crop.setProducer(cropInfo[i][3]);
            crop.setBreeder("研究员" + (i + 1));
            crop.setGerminationRate(Math.round((75 + RANDOM.nextDouble() * 20) * 10) / 10.0);
            crop.setShelfLife(LocalDateTime.now().plusMonths(6 + RANDOM.nextInt(12)));
            crop.setSowingSeason(new String[]{"SPRING", "SUMMER", "AUTUMN"}[RANDOM.nextInt(3)]);
            crop.setDiseaseResistance(new String[]{"HIGH", "MEDIUM", "LOW"}[RANDOM.nextInt(3)]);
            crop.setDescription(cropInfo[i][1] + "具有抗病性强、产量稳定等特点。株高适中，抗倒伏能力强。");
            crop.setStatus("ACTIVE");
            crop.setCreatedBy(1L);
            crops.add(crop);
        }

        cropRepository.saveAll(crops);
        System.out.println("生成作物数据: " + crops.size() + " 条");
    }

    /**
     * 生成观测模板测试数据
     */
    private void generateTemplates() {
        if (templateRepository.count() > 0) {
            System.out.println("模板数据已存在，跳过生成");
            return;
        }

        List<ObservationTemplate> templates = new ArrayList<>();

        String cornFields = "[{\"field\":\"plantHeight\",\"label\":\"株高(cm)\",\"type\":\"number\",\"required\":true},{\"field\":\"emergenceRate\",\"label\":\"出苗率(%)\",\"type\":\"number\",\"required\":false},{\"field\":\"pestLevel\",\"label\":\"病虫害等级\",\"type\":\"select\",\"options\":[\"0-无\",\"1-轻度\",\"2-中度\",\"3-重度\",\"4-严重\"],\"required\":true},{\"field\":\"leafColor\",\"label\":\"叶色\",\"type\":\"select\",\"options\":[\"深绿\",\"绿色\",\"浅绿\",\"黄色\"],\"required\":false}]";

        String wheatFields = "[{\"field\":\"plantHeight\",\"label\":\"株高(cm)\",\"type\":\"number\",\"required\":true},{\"field\":\"tillerCount\",\"label\":\"分蘖数\",\"type\":\"number\",\"required\":false},{\"field\":\"earLength\",\"label\":\"穗长(cm)\",\"type\":\"number\",\"required\":false},{\"field\":\"pestLevel\",\"label\":\"病虫害等级\",\"type\":\"select\",\"options\":[\"0-无\",\"1-轻度\",\"2-中度\",\"3-重度\",\"4-严重\"],\"required\":true}]";

        String riceFields = "[{\"field\":\"plantHeight\",\"label\":\"株高(cm)\",\"type\":\"number\",\"required\":true},{\"field\":\"tillerCount\",\"label\":\"有效分蘖数\",\"type\":\"number\",\"required\":false},{\"field\":\"panicleLength\",\"label\":\"穗长(cm)\",\"type\":\"number\",\"required\":false},{\"field\":\"emptyGrains\",\"label\":\"空粒数\",\"type\":\"number\",\"required\":false},{\"field\":\"pestLevel\",\"label\":\"病虫害等级\",\"type\":\"select\",\"options\":[\"0-无\",\"1-轻度\",\"2-中度\",\"3-重度\",\"4-严重\"],\"required\":true}]";

        ObservationTemplate cornTemplate = new ObservationTemplate();
        cornTemplate.setTemplateName("玉米标准观测模板");
        cornTemplate.setCropType("CORN");
        cornTemplate.setApplicableStages("SEEDLING,JOINTING,HEADING,FILLING,MATURING");
        cornTemplate.setFieldsConfig(cornFields);
        cornTemplate.setIsSystem(true);
        cornTemplate.setDescription("玉米育种试验标准观测项模板");
        cornTemplate.setStatus("ACTIVE");
        templates.add(cornTemplate);

        ObservationTemplate wheatTemplate = new ObservationTemplate();
        wheatTemplate.setTemplateName("小麦标准观测模板");
        wheatTemplate.setCropType("WHEAT");
        wheatTemplate.setApplicableStages("SEEDLING,TILLERING,JOINTING,BOOTING,HEADING,FILLING,MATURING");
        wheatTemplate.setFieldsConfig(wheatFields);
        wheatTemplate.setIsSystem(true);
        wheatTemplate.setDescription("小麦育种试验标准观测项模板");
        wheatTemplate.setStatus("ACTIVE");
        templates.add(wheatTemplate);

        ObservationTemplate riceTemplate = new ObservationTemplate();
        riceTemplate.setTemplateName("水稻标准观测模板");
        riceTemplate.setCropType("RICE");
        riceTemplate.setApplicableStages("SEEDLING,TILLERING,JOINTING,BOOTING,HEADING,FILLING,MATURING");
        riceTemplate.setFieldsConfig(riceFields);
        riceTemplate.setIsSystem(true);
        riceTemplate.setDescription("水稻育种试验标准观测项模板");
        riceTemplate.setStatus("ACTIVE");
        templates.add(riceTemplate);

        templateRepository.saveAll(templates);
        System.out.println("生成模板数据: " + templates.size() + " 条");
    }

    /**
     * 生成田间记录测试数据
     */
    private void generateFieldRecords() {
        if (fieldRecordRepository.count() > 0) {
            System.out.println("田间记录数据已存在，跳过生成");
            return;
        }

        List<FieldRecord> records = new ArrayList<>();
        List<Plot> plots = plotRepository.findAll();
        List<Crop> crops = cropRepository.findAll();
        List<User> users = userRepository.findAll();

        String[] stages = {"SEEDLING", "TILLERING", "JOINTING", "BOOTING", "HEADING", "FLOWERING", "FILLING", "MATURING"};
        String[] weathers = {"SUNNY", "CLOUDY", "RAINY"};
        String[] leafColors = {"DARK_GREEN", "GREEN", "LIGHT_GREEN", "YELLOW"};
        String[] pestTypes = {"无", "蚜虫", "红蜘蛛", "纹枯病", "锈病", "白粉病", "稻瘟病"};

        for (int i = 0; i < 30; i++) {
            FieldRecord record = new FieldRecord();
            record.setRecordNo(generateRecordNo(i + 1));
            
            Plot plot = plots.get(RANDOM.nextInt(plots.size()));
            record.setPlotId(plot.getId());
            record.setPlotName(plot.getPlotName());
            
            Crop crop = crops.get(RANDOM.nextInt(crops.size()));
            record.setCropId(crop.getId());
            record.setCropName(crop.getCropName());
            
            String stage = stages[RANDOM.nextInt(stages.length)];
            record.setGrowthStage(stage);
            
            User user = users.get(1 + RANDOM.nextInt(users.size() - 1));
            record.setObserver(user.getRealName());
            record.setObserverId(user.getId());
            
            record.setRecordDate(LocalDateTime.now().minusDays(RANDOM.nextInt(90)));
            record.setPlantHeight(BigDecimal.valueOf(50 + RANDOM.nextDouble() * 150));
            record.setEmergenceRate(BigDecimal.valueOf(70 + RANDOM.nextDouble() * 30));
            record.setPestLevel(RANDOM.nextInt(5));
            record.setPestType(pestTypes[RANDOM.nextInt(pestTypes.length)]);
            record.setLeafColor(leafColors[RANDOM.nextInt(leafColors.length)]);
            record.setGrowthDescription("生长" + (RANDOM.nextBoolean() ? "良好" : "一般"));
            record.setWeatherCondition(weathers[RANDOM.nextInt(weathers.length)]);
            record.setTemperature(BigDecimal.valueOf(15 + RANDOM.nextDouble() * 20));
            record.setHumidity(BigDecimal.valueOf(40 + RANDOM.nextDouble() * 40));
            record.setDataSource(RANDOM.nextBoolean() ? "ONLINE" : "OFFLINE");
            record.setStatus(RANDOM.nextInt(10) == 0 ? "DRAFT" : "SUBMITTED");
            record.setRemarks("这是第" + (i + 1) + "条测试田间记录");
            records.add(record);
        }

        fieldRecordRepository.saveAll(records);
        System.out.println("生成田间记录数据: " + records.size() + " 条");
    }

    /**
     * 生成病虫害图片测试数据
     */
    private void generatePestImages() {
        if (pestImageRepository.count() > 0) {
            System.out.println("图片数据已存在，跳过生成");
            return;
        }

        List<PestImage> images = new ArrayList<>();
        List<FieldRecord> records = fieldRecordRepository.findAll();
        List<User> users = userRepository.findAll();

        String[] imageTypes = {"PEST", "PLANT", "LEAF", "ROOT"};
        String[] imageNames = {"病虫害特写", "植株全貌", "叶片细节", "根系观察"};

        for (FieldRecord record : records) {
            if (RANDOM.nextBoolean()) {
                int imageCount = 1 + RANDOM.nextInt(3);
                for (int j = 0; j < imageCount; j++) {
                    PestImage image = new PestImage();
                    image.setFieldRecordId(record.getId());
                    image.setImageName(imageNames[RANDOM.nextInt(imageNames.length)]);
                    image.setImagePath("/uploads/field/" + record.getId() + "_" + j + ".jpg");
                    image.setImageType(imageTypes[RANDOM.nextInt(imageTypes.length)]);
                    image.setDescription("田间记录" + record.getRecordNo() + "的图片");
                    image.setCaptureTime(record.getRecordDate());
                    image.setLongitude(113.0 + RANDOM.nextDouble() * 5);
                    image.setLatitude(30.0 + RANDOM.nextDouble() * 5);
                    image.setUploadedBy(users.get(RANDOM.nextInt(users.size())).getId());
                    image.setStatus("ACTIVE");
                    images.add(image);
                }
            }
        }

        pestImageRepository.saveAll(images);
        System.out.println("生成图片数据: " + images.size() + " 条");
    }

    /**
     * 生成气象数据测试数据
     */
    private void generateWeatherData() {
        if (weatherDataRepository.count() > 0) {
            System.out.println("气象数据已存在，跳过生成");
            return;
        }

        List<WeatherData> weatherDataList = new ArrayList<>();
        List<Plot> plots = plotRepository.findAll();

        String[] weathers = {"SUNNY", "PARTLY_CLOUDY", "CLOUDY", "RAINY"};
        String[] windDirections = {"N", "S", "E", "W", "NE", "SE", "NW", "SW"};

        String[] cities = {"101180101", "101180301", "101120101", "101090101", "101190101"};
        String[] cityNames = {"郑州", "新乡", "济南", "石家庄", "南京"};

        for (int i = 0; i < plots.size(); i++) {
            for (int j = 0; j < 7; j++) {
                WeatherData wd = new WeatherData();
                wd.setPlotId(plots.get(i).getId());
                wd.setCityCode(cities[i % cities.length]);
                wd.setCityName(cityNames[i % cityNames.length]);
                wd.setRecordDate(LocalDateTime.now().minusDays(j));
                wd.setWeatherCondition(weathers[RANDOM.nextInt(weathers.length)]);
                double temp = 15 + RANDOM.nextDouble() * 15;
                wd.setTemperature(BigDecimal.valueOf(temp));
                wd.setFeelsLike(BigDecimal.valueOf(temp - 1 + RANDOM.nextDouble() * 2));
                wd.setTempMin(BigDecimal.valueOf(8 + RANDOM.nextDouble() * 10));
                wd.setTempMax(BigDecimal.valueOf(25 + RANDOM.nextDouble() * 10));
                wd.setHumidity(BigDecimal.valueOf(40 + RANDOM.nextDouble() * 40));
                wd.setWindDirection(windDirections[RANDOM.nextInt(windDirections.length)]);
                wd.setWindSpeed(BigDecimal.valueOf(1 + RANDOM.nextDouble() * 5));
                wd.setCloudCover(BigDecimal.valueOf(RANDOM.nextDouble() * 80));
                wd.setPrecipitation(BigDecimal.valueOf(RANDOM.nextDouble() * 30));
                wd.setUvIndex(RANDOM.nextInt(3) == 0 ? "弱" : "中等");
                wd.setAqi(30 + RANDOM.nextInt(120));
                wd.setDataSource("API");
                weatherDataList.add(wd);
            }
        }

        weatherDataRepository.saveAll(weatherDataList);
        System.out.println("生成气象数据: " + weatherDataList.size() + " 条");
    }

    /**
     * 生成客户测试数据
     */
    private void generateCustomers() {
        if (customerRepository.count() > 0) {
            System.out.println("客户数据已存在，跳过生成");
            return;
        }

        List<Customer> customers = new ArrayList<>();

        String[][] customerInfo = {
            {"CS001", "中农种业科技有限公司", "COMPANY", "VIP"},
            {"CS002", "金丰农业发展集团", "COMPANY", "VIP"},
            {"CS003", "河南省农科院", "RESEARCH", "NORMAL"},
            {"CS004", "山东农业大学", "RESEARCH", "NORMAL"},
            {"CS005", "李向阳", "INDIVIDUAL", "NORMAL"},
            {"CS006", "河南种子公司", "COMPANY", "VIP"},
            {"CS007", "山东登海种业", "COMPANY", "NORMAL"},
            {"CS008", "黑龙江北大荒集团", "COMPANY", "VIP"}
        };

        String[] regions = {"华北区", "华东区", "华中区", "西南区", "东北区"};

        for (int i = 0; i < customerInfo.length; i++) {
            Customer customer = new Customer();
            customer.setCustomerCode(customerInfo[i][0]);
            customer.setCustomerName(customerInfo[i][1]);
            customer.setCustomerType(customerInfo[i][2]);
            customer.setCustomerLevel(customerInfo[i][3]);
            customer.setContactPerson(encryptionUtil.encrypt("联系人" + (i + 1)));
            customer.setPhone(encryptionUtil.encrypt("1860000000" + (i + 1)));
            customer.setEmail(encryptionUtil.encrypt("customer" + (i + 1) + "@company.com"));
            customer.setAddress(regions[RANDOM.nextInt(regions.length)] + "某市某路" + (i + 1) + "号");
            customer.setRegion(regions[RANDOM.nextInt(regions.length)]);
            customer.setRemarks("这是测试客户数据");
            customer.setStatus("ACTIVE");
            customer.setCreatedBy(1L);
            customers.add(customer);
        }

        customerRepository.saveAll(customers);
        System.out.println("生成客户数据: " + customers.size() + " 条");
    }

    /**
     * 生成批次编号（8位数字+字母）
     */
    private String generateBatchCode(int index) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        sb.append("BC");
        sb.append(String.format("%04d", index));
        for (int i = 0; i < 2; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString().substring(0, 8);
    }

    /**
     * 生成记录编号
     */
    private String generateRecordNo(int index) {
        return "FR" + System.currentTimeMillis() + String.format("%04d", index);
    }
}
