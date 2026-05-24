package com.plate.config;

import com.plate.entity.Blacklist;
import com.plate.entity.Camera;
import com.plate.entity.RecognitionRecord;
import com.plate.repository.BlacklistRepository;
import com.plate.repository.CameraRepository;
import com.plate.repository.RecognitionRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private RecognitionRecordRepository recordRepository;

    @Autowired
    private BlacklistRepository blacklistRepository;

    private Random random = new Random();

    private String[] plateNumbers = {
            "京A12345", "沪B67890", "粤C11111", "苏D22222", "浙E33333",
            "川F44444", "鲁G55555", "豫H66666", "冀J77777", "湘K88888"
    };

    private String[] cameraNames = {
            "东大门入口", "西大门出口", "北门停车场", "南门广场",
            "地下车库A区", "地下车库B区", "一号楼入口", "二号楼入口"
    };

    private String[] anomalyTypes = {
            "超速行驶", "闯红灯", "违章停车", "遮挡号牌", null, null, null, null
    };

    @Override
    public void run(String... args) {
        initCameras();
        initRecords();
        initBlacklist();
    }

    private void initCameras() {
        if (cameraRepository.count() == 0) {
            double baseLng = 116.397;
            double baseLat = 39.908;

            for (int i = 0; i < cameraNames.length; i++) {
                Camera camera = new Camera();
                camera.setCameraId("CAM" + String.format("%03d", i + 1));
                camera.setName(cameraNames[i]);
                camera.setLocation("北京市朝阳区" + cameraNames[i]);
                camera.setLongitude(baseLng + (random.nextDouble() - 0.5) * 0.1);
                camera.setLatitude(baseLat + (random.nextDouble() - 0.5) * 0.1);
                camera.setIsOnline(random.nextBoolean() || i < 5);
                camera.setNightMode(false);
                camera.setStreamUrl("rtsp://stream.example.com/cam" + (i + 1));
                cameraRepository.save(camera);
            }
        }
    }

    private void initRecords() {
        if (recordRepository.count() == 0) {
            java.util.List<Camera> cameras = cameraRepository.findAll();

            for (int i = 0; i < 50; i++) {
                RecognitionRecord record = new RecognitionRecord();
                Camera camera = cameras.get(random.nextInt(cameras.size()));
                String plate = plateNumbers[random.nextInt(plateNumbers.length)];

                record.setPlateNumber(plate);
                record.setCameraId(camera.getCameraId());
                record.setCameraName(camera.getName());
                record.setPassTime(LocalDateTime.now().minusHours(random.nextInt(48)));
                record.setConfidence(80 + random.nextDouble() * 20);
                record.setImageUrl("https://picsum.photos/400/300?random=" + i);

                String anomalyType = anomalyTypes[random.nextInt(anomalyTypes.length)];
                if (anomalyType != null) {
                    record.setAnomalyType(anomalyType);
                    record.setIsAnomaly(true);
                } else {
                    record.setIsAnomaly(false);
                }

                recordRepository.save(record);
            }
        }
    }

    private void initBlacklist() {
        if (blacklistRepository.count() == 0) {
            String[] blackPlates = {"京A99999", "沪B88888", "粤C77777"};
            String[] reasons = {"多次违章未处理", "涉嫌肇事逃逸", "欠费车辆"};

            for (int i = 0; i < blackPlates.length; i++) {
                Blacklist blacklist = new Blacklist();
                blacklist.setPlateNumber(blackPlates[i]);
                blacklist.setReason(reasons[i]);
                blacklist.setEvidenceImage("https://picsum.photos/400/300?random=black" + i);
                blacklist.setExpireAt(LocalDateTime.now().plusDays(30));
                blacklist.setIsActive(true);
                blacklist.setCreatedBy("admin");
                blacklistRepository.save(blacklist);
            }
        }
    }
}
