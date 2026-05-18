package com.culturalrelic.config;

import com.culturalrelic.entity.*;
import com.culturalrelic.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 数据初始化器
 * 系统启动时自动初始化测试数据
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CulturalRelicRepository culturalRelicRepository;
    @Autowired
    private BlockchainNodeRepository blockchainNodeRepository;
    @Autowired
    private IotDeviceRepository iotDeviceRepository;
    @Autowired
    private AuditRuleRepository auditRuleRepository;
    @Autowired
    private DigitalTwinRepository digitalTwinRepository;
    @Autowired
    private VrSceneRepository vrSceneRepository;
    @Autowired
    private RelicRecognitionModelRepository relicRecognitionModelRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run(String... args) throws Exception {
        initCulturalRelics();
        initBlockchainNodes();
        initIotDevices();
        initAuditRules();
        initDigitalTwins();
        initVrScenes();
        initRecognitionModels();
        System.out.println("========================================");
        System.out.println("  测试数据初始化完成！");
        System.out.println("========================================");
    }

    /**
     * 初始化文物测试数据
     */
    private void initCulturalRelics() {
        String[][] relics = {
            {"RL001", "清代官窑青花瓷瓶", "瓷器", "清代", "官窑", "高35cm，口径12cm", "2.5", "清代乾隆年间官窑精品，保存完好", "0", "故宫博物院", "张馆长"},
            {"RL002", "商代青铜鼎", "青铜器", "商代", "后母戊鼎", "高133cm，长110cm", "832.84", "商代晚期青铜重器，国家一级文物", "0", "国家博物馆", "李馆长"},
            {"RL003", "清明上河图", "书画", "北宋", "张择端", "长528cm，宽24.8cm", "0.5", "北宋风俗画，中国十大传世名画之一", "0", "故宫博物院", "王馆长"},
            {"RL004", "越王勾践剑", "青铜器", "春秋", "越国", "全长55.7cm", "0.875", "春秋晚期越国青铜器，被誉为天下第一剑", "0", "湖北省博物馆", "赵馆长"},
            {"RL005", "曾侯乙编钟", "青铜器", "战国", "曾国", "长748cm，高265cm", "2567", "战国早期大型礼乐重器", "0", "湖北省博物馆", "赵馆长"},
            {"RL006", "金缕玉衣", "玉器", "汉代", "西汉", "长188cm", "1.5", "汉代皇帝和高级贵族死后穿用的殓服", "0", "河北省博物馆", "刘馆长"},
            {"RL007", "马踏飞燕", "青铜器", "东汉", "武威雷台", "高34.5cm，长45cm", "7.15", "东汉青铜器，中国旅游标志", "0", "甘肃省博物馆", "孙馆长"},
            {"RL008", "镶金兽首玛瑙杯", "玉器", "唐代", "何家村", "高6.5cm，长15.6cm", "0.45", "唐代玉器精品，海内孤品", "0", "陕西历史博物馆", "周馆长"}
        };

        for (String[] r : relics) {
            CulturalRelic relic = new CulturalRelic();
            relic.setRelicNo(r[0]);
            relic.setName(r[1]);
            relic.setCategory(r[2]);
            relic.setDynasty(r[3]);
            relic.setPeriod(r[4]);
            relic.setSize(r[5]);
            relic.setWeight(Double.parseDouble(r[6]));
            relic.setDescription(r[7]);
            relic.setStatus(Integer.parseInt(r[8]));
            relic.setLocation(r[9]);
            relic.setResponsiblePerson(r[10]);
            relic.setCollectDate("2020-01-01");
            culturalRelicRepository.save(relic);
        }
    }

    /**
     * 初始化区块链节点测试数据
     */
    private void initBlockchainNodes() {
        String[][] nodes = {
            {"主节点-北京", "192.168.1.101", "8545", "1", "cn-beijing-a", "华北-北京", "1", "http", "v2.7.0", "1000000"},
            {"主节点-上海", "192.168.2.101", "8545", "1", "cn-shanghai-a", "华东-上海", "1", "http", "v2.7.0", "1000000"},
            {"备份节点-广州", "192.168.3.102", "8545", "2", "cn-guangzhou-b", "华南-广州", "1", "http", "v2.7.0", "999999"},
            {"备份节点-成都", "192.168.4.102", "8545", "2", "cn-chengdu-a", "西南-成都", "1", "http", "v2.7.0", "999999"},
            {"同步节点-深圳", "192.168.5.103", "8545", "3", "cn-shenzhen-b", "华南-深圳", "1", "https", "v2.7.0", "999998"}
        };

        for (String[] n : nodes) {
            BlockchainNode node = new BlockchainNode();
            node.setNodeName(n[0]);
            node.setNodeAddress(n[1]);
            node.setPort(Integer.parseInt(n[2]));
            node.setNodeType(Integer.parseInt(n[3]));
            node.setAvailabilityZone(n[4]);
            node.setRegion(n[5]);
            node.setStatus(Integer.parseInt(n[6]));
            node.setProtocol(n[7]);
            node.setVersion(n[8]);
            node.setBlockHeight(Long.parseLong(n[9]));
            node.setEnabled(1);
            blockchainNodeRepository.save(node);
        }
    }

    /**
     * 初始化物联网设备测试数据
     */
    private void initIotDevices() {
        String[][] devices = {
            {"DEV001", "展厅A温湿度传感器", "1", "NFC001-A001", "1", "1", "22.5", "45.0"},
            {"DEV002", "展厅B温湿度传感器", "1", "NFC002-A002", "2", "1", "21.8", "48.0"},
            {"DEV003", "展厅C温湿度传感器", "1", "NFC003-A003", "3", "1", "23.2", "42.0"},
            {"DEV004", "库房1温湿度传感器", "1", "NFC004-B001", "4", "0", "20.0", "50.0"},
            {"DEV005", "库房2温湿度传感器", "1", "NFC005-B002", "5", "0", "19.5", "52.0"},
            {"DEV006", "修复室温湿度传感器", "1", "NFC006-C001", null, "0", "24.0", "40.0"}
        };

        for (String[] d : devices) {
            IotDevice device = new IotDevice();
            device.setDeviceNo(d[0]);
            device.setDeviceName(d[1]);
            device.setDeviceType(Integer.parseInt(d[2]));
            device.setNfcTagId(d[3]);
            if (d[4] != null) {
                device.setRelicId(Long.parseLong(d[4]));
                device.setBound(Integer.parseInt(d[5]));
            }
            device.setCurrentTemperature(Double.parseDouble(d[6]));
            device.setCurrentHumidity(Double.parseDouble(d[7]));
            device.setStatus(1);
            device.setMinTemperature(10.0);
            device.setMaxTemperature(30.0);
            device.setMinHumidity(30.0);
            device.setMaxHumidity(70.0);
            device.setAlarmStatus(0);
            device.setManufacturer("华为技术有限公司");
            device.setModel("WS5000");
            device.setFirmwareVersion("v1.2.3");
            device.setInstallLocation(d[1].contains("展厅") ? "展厅" : "库房");
            iotDeviceRepository.save(device);
        }
    }

    /**
     * 初始化审核规则测试数据
     */
    private void initAuditRules() {
        String[][] rules = {
            {"AR001", "文物入库审核规则", "1", "2", "3", "张审核", "一级文物入库需三级审核", "v1.0"},
            {"AR002", "文物出库审核规则", "2", "2", "2", "李审核", "文物出库需二级审核", "v1.0"},
            {"AR003", "文物修复审核规则", "3", "1", "3", "王审核", "修复方案需专家评审", "v1.0"},
            {"AR004", "文物展览审核规则", "4", "2", "2", "赵审核", "展览需文物安全评估", "v1.0"}
        };

        for (String[] r : rules) {
            AuditRule rule = new AuditRule();
            rule.setRuleNo(r[0]);
            rule.setRuleName(r[1]);
            rule.setRuleType(Integer.parseInt(r[2]));
            rule.setPriority(Integer.parseInt(r[3]));
            rule.setAuditLevel(Integer.parseInt(r[4]));
            rule.setAuditorRole(r[5]);
            rule.setDescription(r[6]);
            rule.setVersion(r[7]);
            rule.setStatus(1);
            rule.setTimeoutHours(48);
            auditRuleRepository.save(rule);
        }
    }

    /**
     * 初始化数字分身测试数据
     */
    private void initDigitalTwins() {
        String[][] twins = {
            {"1", "清代官窑青花瓷瓶", "数字分身-青花瓷瓶", "/models/blue_porcelain.glb", "glb", "156.5", "/textures/blue_porcelain.png", "1", "3"},
            {"2", "商代青铜鼎", "数字分身-青铜鼎", "/models/bronze_ding.glb", "glb", "520.3", "/textures/bronze_ding.png", "1", "3"},
            {"3", "清明上河图", "数字分身-清明上河图", "/models/qingming.glb", "glb", "89.2", "/textures/qingming.png", "0", "2"},
            {"4", "越王勾践剑", "数字分身-勾践剑", "/models/goujian_sword.glb", "glb", "78.6", "/textures/goujian_sword.png", "1", "3"}
        };

        for (String[] t : twins) {
            DigitalTwin twin = new DigitalTwin();
            twin.setRelicId(Long.parseLong(t[0]));
            twin.setRelicName(t[1]);
            twin.setTwinName(t[2]);
            twin.setModelPath(t[3]);
            twin.setModelFormat(t[4]);
            twin.setModelSize(Double.parseDouble(t[5]));
            twin.setTexturePath(t[6]);
            twin.setSupportRestorationDemo(Integer.parseInt(t[7]));
            twin.setPrecisionLevel(Integer.parseInt(t[8]));
            twin.setStatus(1);
            twin.setVersion("v1.0");
            digitalTwinRepository.save(twin);
        }
    }

    /**
     * 初始化VR场景测试数据
     */
    private void initVrScenes() {
        String[][] scenes = {
            {"VR001", "虚拟博物馆-主展厅", "1", "/vr/scene_main.glb", "glb", "/vr/thumb_main.jpg", "/vr/panorama_main.jpg", "[1,2,3,4]", "4", "1"},
            {"VR002", "青铜器专题馆", "2", "/vr/scene_bronze.glb", "glb", "/vr/thumb_bronze.jpg", "/vr/panorama_bronze.jpg", "[2,4,5]", "3", "1"},
            {"VR003", "瓷器专题馆", "2", "/vr/scene_porcelain.glb", "glb", "/vr/thumb_porcelain.jpg", "/vr/panorama_porcelain.jpg", "[1]", "1", "1"},
            {"VR004", "书画艺术长廊", "3", "/vr/scene_painting.glb", "glb", "/vr/thumb_painting.jpg", "/vr/panorama_painting.jpg", "[3]", "1", "1"}
        };

        for (String[] s : scenes) {
            VrScene scene = new VrScene();
            scene.setSceneNo(s[0]);
            scene.setSceneName(s[1]);
            scene.setSceneType(Integer.parseInt(s[2]));
            scene.setScenePath(s[3]);
            scene.setSceneFormat(s[4]);
            scene.setThumbnailUrl(s[5]);
            scene.setPanoramaUrl(s[6]);
            scene.setRelicIds(s[7]);
            scene.setRelicCount(Integer.parseInt(s[8]));
            scene.setSupportVrDevice(Integer.parseInt(s[9]));
            scene.setStatus(1);
            scene.setVisitCount(0L);
            vrSceneRepository.save(scene);
        }
    }

    /**
     * 初始化识别模型测试数据
     */
    private void initRecognitionModels() {
        String[][] models = {
            {"MOD001", "文物分类模型-ResNet50", "v3.2", "1", "/models/relic_classify.pth", "pytorch", "98.5", "6", "1", "1"},
            {"MOD002", "文物检测模型-YOLOv5", "v2.1", "2", "/models/relic_detect.pt", "pytorch", "156.2", "8", "0", "1"},
            {"MOD003", "文物特征提取模型", "v1.5", "3", "/models/feature_extract.onnx", "onnx", "45.8", "12", "0", "1"}
        };

        for (String[] m : models) {
            RelicRecognitionModel model = new RelicRecognitionModel();
            model.setModelNo(m[0]);
            model.setModelName(m[1]);
            model.setModelVersion(m[2]);
            model.setModelType(Integer.parseInt(m[3]));
            model.setModelPath(m[4]);
            model.setFramework(m[5]);
            model.setModelSize(Double.parseDouble(m[6]));
            model.setAccuracy(92.5);
            model.setCategoryCount(Integer.parseInt(m[7]));
            model.setIsDefault(Integer.parseInt(m[8]));
            model.setStatus(Integer.parseInt(m[9]));
            relicRecognitionModelRepository.save(model);
        }
    }
}
