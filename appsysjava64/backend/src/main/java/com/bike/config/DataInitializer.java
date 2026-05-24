package com.bike.config;

import com.bike.entity.*;
import com.bike.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 数据初始化器 - 应用启动时自动生成测试数据
 * 
 * @author bike-sharing
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AreaService areaService;

    @Autowired
    private BikeService bikeService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private DispatchService dispatchService;

    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private BatteryService batteryService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("开始初始化测试数据...");
        
        initAreas();
        initBikes();
        initStaffs();
        initSpareParts();
        initBatteries();
        initSwapStations();
        initHeatPoints();
        initDispatchTasks();
        initRepairOrders();
        initFaultRecords();
        
        System.out.println("测试数据初始化完成！");
    }

    private void initAreas() {
        String[][] areaData = {
            {"中心广场", "116.403874", "39.914885", "BUSINESS", "9"},
            {"火车站", "116.427057", "39.908463", "STATION", "10"},
            {"科技园", "116.317660", "39.988849", "BUSINESS", "8"},
            {"大学城", "116.350444", "39.948849", "SCHOOL", "7"},
            {"居民区A", "116.450444", "39.958849", "RESIDENTIAL", "6"},
            {"居民区B", "116.380444", "39.928849", "RESIDENTIAL", "5"},
            {"地铁站A", "116.410444", "39.918849", "STATION", "9"},
            {"地铁站B", "116.430444", "39.938849", "STATION", "8"},
            {"商圈A", "116.390444", "39.908849", "BUSINESS", "8"},
            {"公园", "116.370444", "39.968849", "PARK", "4"}
        };

        for (String[] data : areaData) {
            Area area = new Area();
            area.setAreaName(data[0]);
            area.setCenterLongitude(Double.parseDouble(data[1]));
            area.setCenterLatitude(Double.parseDouble(data[2]));
            area.setAreaType(data[3]);
            area.setDemandLevel(Integer.parseInt(data[4]));
            area.setBikeCount((int) (Math.random() * 30 + 10));
            areaService.createArea(area);
        }
        System.out.println("区域数据初始化完成");
    }

    private void initBikes() {
        String[] types = {"STANDARD", "ELECTRIC"};
        String[] statuses = {"AVAILABLE", "IN_USE", "FAULT", "MAINTENANCE"};
        String[] locations = {"中心广场附近", "火车站出口", "科技园门口", "大学城东门", "居民区A北门"};

        for (int i = 1; i <= 50; i++) {
            Bike bike = new Bike();
            boolean isElectric = i <= 25;
            bike.setType(types[isElectric ? 1 : 0]);
            bike.setIsElectric(isElectric);
            bike.setStatus(statuses[i % 4]);
            bike.setLongitude(116.35 + Math.random() * 0.15);
            bike.setLatitude(39.90 + Math.random() * 0.1);
            bike.setLocation(locations[i % 5]);
            bike.setCurrentAreaId((long) (i % 10 + 1));
            if (isElectric) {
                bike.setBatteryLevel((int) (Math.random() * 80 + 20));
            }
            bike.setTotalRideCount((int) (Math.random() * 500));
            bike.setTotalMileage((int) (Math.random() * 2000));
            bikeService.createBike(bike);
        }
        System.out.println("车辆数据初始化完成");
    }

    private void initStaffs() {
        String[][] staffData = {
            {"张三", "13800138001", "123456", "中心区域"},
            {"李四", "13800138002", "123456", "东区"},
            {"王五", "13800138003", "123456", "西区"},
            {"赵六", "13800138004", "123456", "南区"},
            {"钱七", "13800138005", "123456", "北区"}
        };

        for (String[] data : staffData) {
            MaintenanceStaff staff = new MaintenanceStaff();
            staff.setName(data[0]);
            staff.setPhone(data[1]);
            staff.setPassword(data[2]);
            staff.setWorkArea(data[3]);
            staff.setStatus("ONLINE");
            staff.setLongitude(116.38 + Math.random() * 0.08);
            staff.setLatitude(39.92 + Math.random() * 0.06);
            staff.setCompletedTasks((int) (Math.random() * 200));
            staff.setRating(4.5f + (float) Math.random() * 0.5f);
            staffService.createStaff(staff);
        }
        System.out.println("运维人员数据初始化完成");
    }

    private void initSpareParts() {
        String[][] partsData = {
            {"链条", "DRIVE", "单速链条", "条", "25.5", "100", "20"},
            {"轮胎", "WHEEL", "26寸充气轮胎", "个", "45.0", "80", "15"},
            {"内胎", "WHEEL", "26寸丁基胶内胎", "条", "15.0", "120", "30"},
            {"刹车片", "BRAKE", "碟刹刹车片", "副", "35.0", "60", "10"},
            {"刹车线", "BRAKE", "不锈钢刹车线", "根", "8.5", "150", "40"},
            {"电池", "BATTERY", "36V锂电池", "块", "580.0", "30", "5"},
            {"车座", "SEAT", "舒适型车座", "个", "42.0", "50", "10"},
            {"脚踏", "DRIVE", "防滑脚踏", "对", "28.0", "70", "15"},
            {"车把", "HANDLE", "铝合金车把", "根", "65.0", "40", "8"},
            {"车架", "FRAME", "铝合金车架", "个", "280.0", "20", "3"}
        };

        for (String[] data : partsData) {
            SparePart part = new SparePart();
            part.setPartName(data[0]);
            part.setCategory(data[1]);
            part.setSpecification(data[2]);
            part.setUnit(data[3]);
            part.setUnitPrice(Float.parseFloat(data[4]));
            part.setStockQuantity(Integer.parseInt(data[5]));
            part.setMinStock(Integer.parseInt(data[6]));
            part.setStatus("ACTIVE");
            part.setSupplier(data[0] + "供应商");
            maintenanceService.createSparePart(part);
        }
        System.out.println("备件数据初始化完成");
    }

    private void initBatteries() {
        for (int i = 1; i <= 30; i++) {
            Battery battery = new Battery();
            battery.setModel("36V-10AH-" + i);
            battery.setCapacity(10000);
            battery.setCurrentLevel((int) (Math.random() * 60 + 40));
            battery.setHealthDegree(85.0f + (float) (Math.random() * 15));
            battery.setChargeCount((int) (Math.random() * 80));
            battery.setTotalChargeCount((int) (Math.random() * 120));
            battery.setStatus(i <= 20 ? "IN_USE" : (i <= 25 ? "CHARGING" : "AVAILABLE"));
            if (i <= 20) {
                battery.setCurrentBikeId((long) i);
            } else if (i <= 25) {
                battery.setCurrentStationId(1L);
            } else {
                battery.setCurrentStationId(1L);
            }
            battery.setManufactureDate(LocalDateTime.now().minusMonths((int) (Math.random() * 12 + 1)));
            batteryService.createBattery(battery);
        }
        System.out.println("电池数据初始化完成");
    }

    private void initSwapStations() {
        String[][] stationsData = {
            {"中心广场换电站", "北京市朝阳区中心广场A1", "116.403874", "39.914885", "20"},
            {"火车站换电站", "北京市东城区火车站北出口", "116.427057", "39.908463", "15"},
            {"科技园换电站", "北京市海淀区科技园西门", "116.317660", "39.988849", "25"},
            {"大学城换电站", "北京市海淀区大学城服务中心", "116.350444", "39.948849", "18"}
        };

        for (String[] data : stationsData) {
            SwapStation station = new SwapStation();
            station.setStationName(data[0]);
            station.setAddress(data[1]);
            station.setLongitude(Double.parseDouble(data[2]));
            station.setLatitude(Double.parseDouble(data[3]));
            station.setTotalSlots(Integer.parseInt(data[4]));
            station.setAvailableBatteries((int) (Math.random() * 10 + 5));
            station.setChargingBatteries((int) (Math.random() * 8 + 2));
            station.setStatus("OPEN");
            station.setBusinessHours("06:00-22:00");
            station.setContactPhone("400-000-" + (1000 + (int) (Math.random() * 9000)));
            batteryService.createSwapStation(station);
        }
        System.out.println("换电站数据初始化完成");
    }

    private void initHeatPoints() {
        String[][] heatData = {
            {"中心广场", "116.403874", "39.914885", "HOTSPOT", "95", "1"},
            {"火车站入口", "116.427057", "39.908463", "STATION", "100", "2"},
            {"科技园A座", "116.317660", "39.988849", "BUSINESS", "88", "3"},
            {"科技园B座", "116.318660", "39.987449", "BUSINESS", "82", "3"},
            {"大学城图书馆", "116.350444", "39.948849", "SCHOOL", "75", "4"},
            {"大学城食堂", "116.351444", "39.947849", "SCHOOL", "80", "4"},
            {"居民区A超市", "116.450444", "39.958849", "RESIDENTIAL", "65", "5"},
            {"地铁站A出口", "116.410444", "39.918849", "STATION", "92", "7"},
            {"地铁站B出口", "116.430444", "39.938849", "STATION", "85", "8"},
            {"商圈A入口", "116.390444", "39.908849", "BUSINESS", "87", "9"},
            {"公园东门", "116.370444", "39.968849", "PARK", "45", "10"},
            {"居民区B门口", "116.380444", "39.928849", "RESIDENTIAL", "55", "6"}
        };

        for (String[] data : heatData) {
            HeatPoint point = new HeatPoint();
            point.setLocationName(data[0]);
            point.setLongitude(Double.parseDouble(data[1]));
            point.setLatitude(Double.parseDouble(data[2]));
            point.setPointType(data[3]);
            point.setHeatValue(Integer.parseInt(data[4]));
            point.setAreaId(Long.parseLong(data[5]));
            point.setRecordTime(LocalDateTime.now());
            dispatchService.addHeatPoint(point);
        }
        System.out.println("热力点数据初始化完成");
    }

    private void initDispatchTasks() {
        String[][] taskData = {
            {"DISPATCH", "URGENT", "将5辆车从居民区A调度到中心广场", "1", "5"},
            {"DISPATCH", "HIGH", "将8辆车从居民区B调度到火车站", "2", "8"},
            {"DISPATCH", "MEDIUM", "将3辆车从公园调度到科技园", "3", "3"},
            {"BATTERY", "URGENT", "低电量电池更换任务", "4", "5"},
            {"MAINTENANCE", "HIGH", "区域车辆巡检任务", "5", "10"}
        };

        for (int i = 0; i < taskData.length; i++) {
            DispatchTask task = new DispatchTask();
            task.setTaskType(taskData[i][0]);
            task.setPriority(taskData[i][1]);
            task.setDescription(taskData[i][2]);
            task.setStatus(i < 3 ? "PENDING" : "ACCEPTED");
            task.setFromAreaId((long) (i + 1));
            task.setFromAreaName("区域" + (i + 1));
            task.setToAreaId((long) (i + 2));
            task.setToAreaName("区域" + (i + 2));
            task.setBikeCount(Integer.parseInt(taskData[i][4]));
            task.setTargetLongitude(116.38 + Math.random() * 0.08);
            task.setTargetLatitude(39.92 + Math.random() * 0.06);
            task.setTargetLocation("目标地点" + (i + 1));
            if (i >= 3) {
                task.setStaffId((long) (i - 2));
                task.setStaffName("运维人员" + (i - 2));
                task.setAcceptTime(LocalDateTime.now().minusHours(1));
            }
            task.setDeadline(LocalDateTime.now().plusHours(4));
            dispatchService.createDispatchTask(task);
        }
        System.out.println("调度任务数据初始化完成");
    }

    private void initRepairOrders() {
        String[][] orderData = {
            {"刹车故障", "刹车失灵，需要更换刹车片", "URGENT"},
            {"轮胎故障", "后轮胎漏气", "HIGH"},
            {"链条故障", "链条脱落", "MEDIUM"},
            {"电池故障", "电池续航下降", "URGENT"},
            {"车座故障", "车座松动", "LOW"}
        };

        for (int i = 0; i < orderData.length; i++) {
            RepairOrder order = new RepairOrder();
            order.setBikeId((long) (i + 1));
            order.setBikeNo("BK00" + (i + 1));
            order.setFaultType(orderData[i][0]);
            order.setFaultDescription(orderData[i][1]);
            order.setPriority(orderData[i][2]);
            order.setStatus(i < 2 ? "PENDING" : (i < 4 ? "ACCEPTED" : "COMPLETED"));
            order.setBikeLocation("位置" + (i + 1));
            order.setBikeLongitude(116.38 + Math.random() * 0.08);
            order.setBikeLatitude(39.92 + Math.random() * 0.06);
            if (i >= 2) {
                order.setStaffId((long) (i - 1));
                order.setStaffName("运维人员" + (i - 1));
                order.setAcceptTime(LocalDateTime.now().minusHours(2));
            }
            if (i >= 4) {
                order.setCompleteTime(LocalDateTime.now().minusMinutes(30));
                order.setRepairDuration(45);
                order.setRepairCost(35.0f);
            }
            maintenanceService.createRepairOrder(order);
        }
        System.out.println("维修工单数据初始化完成");
    }

    private void initFaultRecords() {
        String[][] faultData = {
            {"刹车故障", "刹车完全失灵，存在安全隐患", "用户APP上报"},
            {"轮胎故障", "前轮被扎破", "巡检发现"},
            {"链条故障", "链条异响，需要润滑", "用户上报"},
            {"电池故障", "电量显示不准", "系统检测"},
            {"车座故障", "车座弹簧损坏", "用户上报"}
        };

        for (int i = 0; i < faultData.length; i++) {
            FaultRecord record = new FaultRecord();
            record.setBikeId((long) (i + 1));
            record.setBikeNo("BK00" + (i + 1));
            record.setFaultType(faultData[i][0]);
            record.setFaultDescription(faultData[i][1]);
            record.setStatus(i < 3 ? "REPORTED" : "RESOLVED");
            record.setReporterId((long) (i + 1));
            record.setReporterName(faultData[i][2]);
            record.setReporterType(i % 2 == 0 ? "USER" : "SYSTEM");
            record.setReportTime(LocalDateTime.now().minusHours(i * 2));
            maintenanceService.createFaultRecord(record);
        }
        System.out.println("故障记录数据初始化完成");
    }
}
