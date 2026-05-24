package com.vehicle.config;

import com.vehicle.entity.Blacklist;
import com.vehicle.entity.Task;
import com.vehicle.entity.Vehicle;
import com.vehicle.repository.BlacklistRepository;
import com.vehicle.repository.TaskRepository;
import com.vehicle.repository.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final VehicleRepository vehicleRepository;
    private final BlacklistRepository blacklistRepository;
    private final TaskRepository taskRepository;

    public DataInitializer(VehicleRepository vehicleRepository,
                          BlacklistRepository blacklistRepository,
                          TaskRepository taskRepository) {
        this.vehicleRepository = vehicleRepository;
        this.blacklistRepository = blacklistRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) {
        initVehicles();
        initBlacklist();
        initTasks();
    }

    private void initVehicles() {
        Vehicle v1 = new Vehicle();
        v1.setPlateNumber("京A12345");
        v1.setBrand("大众");
        v1.setModel("帕萨特");
        v1.setColor("黑色");
        v1.setOwnerName("张三");
        v1.setPhone("13800138000");
        v1.setRegisterDate(LocalDateTime.of(2020, 5, 15, 0, 0));
        vehicleRepository.save(v1);

        Vehicle v2 = new Vehicle();
        v2.setPlateNumber("京B88888");
        v2.setBrand("奔驰");
        v2.setModel("E300L");
        v2.setColor("白色");
        v2.setOwnerName("李四");
        v2.setPhone("13900139000");
        v2.setRegisterDate(LocalDateTime.of(2021, 8, 20, 0, 0));
        vehicleRepository.save(v2);

        Vehicle v3 = new Vehicle();
        v3.setPlateNumber("京C66666");
        v3.setBrand("宝马");
        v3.setModel("5系");
        v3.setColor("银色");
        v3.setOwnerName("王五");
        v3.setPhone("13700137000");
        v3.setRegisterDate(LocalDateTime.of(2019, 3, 10, 0, 0));
        vehicleRepository.save(v3);
    }

    private void initBlacklist() {
        Blacklist b1 = new Blacklist();
        b1.setPlateNumber("京B88888");
        b1.setReason("套牌车辆");
        b1.setDescription("该车辆涉嫌套用其他车辆号牌，已被多次举报");
        b1.setRiskLevel("HIGH");
        b1.setCreator("系统管理员");
        blacklistRepository.save(b1);

        Blacklist b2 = new Blacklist();
        b2.setPlateNumber("京D99999");
        b2.setReason("肇事逃逸");
        b2.setDescription("该车辆涉及一起交通事故后逃逸");
        b2.setRiskLevel("CRITICAL");
        b2.setCreator("李警官");
        blacklistRepository.save(b2);
    }

    private void initTasks() {
        Task t1 = new Task();
        t1.setTaskNo("TASK20240101001");
        t1.setPlateNumber("京C66666");
        t1.setTaskType("SUSPICIOUS_VEHICLE");
        t1.setDescription("该车辆涉嫌套牌，请现场查验核实");
        t1.setPriority("HIGH");
        t1.setLastLocation("北京市海淀区中关村大街");
        t1.setLastLongitude(116.3198);
        t1.setLastLatitude(39.9891);
        t1.setLastSeenTime(LocalDateTime.now().minusHours(2));
        t1.setAssignedOfficer("赵警官");
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setTaskNo("TASK20240101002");
        t2.setPlateNumber("京E55555");
        t2.setTaskType("TRAFFIC_VIOLATION");
        t2.setDescription("该车辆多次闯红灯，请拦截处理");
        t2.setPriority("MEDIUM");
        t2.setLastLocation("北京市朝阳区建国路");
        t2.setLastLongitude(116.4700);
        t2.setLastLatitude(39.9088);
        t2.setLastSeenTime(LocalDateTime.now().minusHours(5));
        t2.setAssignedOfficer("王警官");
        taskRepository.save(t2);
    }
}
