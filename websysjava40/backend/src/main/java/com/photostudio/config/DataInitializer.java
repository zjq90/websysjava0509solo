package com.photostudio.config;

import com.photostudio.entity.*;
import com.photostudio.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 数据初始化器
 * 在应用启动时初始化测试数据
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final ScheduleRepository scheduleRepository;
    private final VenueRepository venueRepository;
    private final VenueBookingRepository venueBookingRepository;
    private final CostumeRepository costumeRepository;
    private final OrderRepository orderRepository;
    private final ExpressRepository expressRepository;

    @Autowired
    public DataInitializer(EmployeeRepository employeeRepository,
                          ScheduleRepository scheduleRepository,
                          VenueRepository venueRepository,
                          VenueBookingRepository venueBookingRepository,
                          CostumeRepository costumeRepository,
                          OrderRepository orderRepository,
                          ExpressRepository expressRepository) {
        this.employeeRepository = employeeRepository;
        this.scheduleRepository = scheduleRepository;
        this.venueRepository = venueRepository;
        this.venueBookingRepository = venueBookingRepository;
        this.costumeRepository = costumeRepository;
        this.orderRepository = orderRepository;
        this.expressRepository = expressRepository;
    }

    @Override
    public void run(String... args) {
        initEmployees();
        initVenues();
        initCostumes();
        initOrders();
        initSchedules();
        initVenueBookings();
        initExpress();
    }

    private void initEmployees() {
        if (employeeRepository.count() == 0) {
            Employee emp1 = new Employee();
            emp1.setName("张三");
            emp1.setEmployeeNo("EMP001");
            emp1.setPosition(Employee.EmployeePosition.PHOTOGRAPHER);
            emp1.setPhone("13800138000");
            emp1.setEmail("zhangsan@photostudio.com");
            employeeRepository.save(emp1);

            Employee emp2 = new Employee();
            emp2.setName("李四");
            emp2.setEmployeeNo("EMP002");
            emp2.setPosition(Employee.EmployeePosition.MAKEUP_ARTIST);
            emp2.setPhone("13800138001");
            emp2.setEmail("lisi@photostudio.com");
            employeeRepository.save(emp2);

            Employee emp3 = new Employee();
            emp3.setName("王五");
            emp3.setEmployeeNo("EMP003");
            emp3.setPosition(Employee.EmployeePosition.FILM_SELECTOR);
            emp3.setPhone("13800138002");
            emp3.setEmail("wangwu@photostudio.com");
            employeeRepository.save(emp3);

            Employee emp4 = new Employee();
            emp4.setName("赵六");
            emp4.setEmployeeNo("EMP004");
            emp4.setPosition(Employee.EmployeePosition.PHOTOGRAPHER);
            emp4.setPhone("13800138003");
            emp4.setEmail("zhaoliu@photostudio.com");
            employeeRepository.save(emp4);

            Employee emp5 = new Employee();
            emp5.setName("钱七");
            emp5.setEmployeeNo("EMP005");
            emp5.setPosition(Employee.EmployeePosition.MANAGER);
            emp5.setPhone("13800138004");
            emp5.setEmail("qianqi@photostudio.com");
            employeeRepository.save(emp5);
        }
    }

    private void initVenues() {
        if (venueRepository.count() == 0) {
            Venue venue1 = new Venue();
            venue1.setName("A棚-海景风格");
            venue1.setVenueNo("VEN001");
            venue1.setType(Venue.VenueType.STUDIO);
            venue1.setAddress("北京市朝阳区XX路XX号");
            venue1.setCapacity(10);
            venue1.setDescription("海景主题影棚，配备专业灯光设备");
            venueRepository.save(venue1);

            Venue venue2 = new Venue();
            venue2.setName("B棚-欧式风格");
            venue2.setVenueNo("VEN002");
            venue2.setType(Venue.VenueType.STUDIO);
            venue2.setAddress("北京市朝阳区XX路XX号");
            venue2.setCapacity(8);
            venue2.setDescription("欧式复古主题影棚");
            venueRepository.save(venue2);

            Venue venue3 = new Venue();
            venue3.setName("外景地-颐和园");
            venue3.setVenueNo("VEN003");
            venue3.setType(Venue.VenueType.OUTDOOR);
            venue3.setAddress("北京市海淀区颐和园路");
            venue3.setCapacity(20);
            venue3.setDescription("皇家园林外景拍摄场地");
            venueRepository.save(venue3);

            Venue venue4 = new Venue();
            venue4.setName("化妆间-A");
            venue4.setVenueNo("VEN004");
            venue4.setType(Venue.VenueType.DRESSING_ROOM);
            venue4.setAddress("北京市朝阳区XX路XX号");
            venue4.setCapacity(4);
            venue4.setDescription("专业化妆间");
            venueRepository.save(venue4);
        }
    }

    private void initCostumes() {
        if (costumeRepository.count() == 0) {
            Costume costume1 = new Costume();
            costume1.setName("白色抹胸婚纱");
            costume1.setCostumeNo("COS001");
            costume1.setType(Costume.CostumeType.WEDDING_DRESS);
            costume1.setSize("M");
            costume1.setColor("白色");
            costume1.setUseCount(5);
            costume1.setCleaningStatus(Costume.CleaningStatus.CLEAN);
            costume1.setDescription("经典白色抹胸婚纱，适合海边拍摄");
            costumeRepository.save(costume1);

            Costume costume2 = new Costume();
            costume2.setName("中式秀禾服");
            costume2.setCostumeNo("COS002");
            costume2.setType(Costume.CostumeType.CHINESE_STYLE);
            costume2.setSize("L");
            costume2.setColor("红色");
            costume2.setUseCount(3);
            costume2.setCleaningStatus(Costume.CleaningStatus.CLEAN);
            costume2.setDescription("中式传统秀禾服，喜庆典雅");
            costumeRepository.save(costume2);

            Costume costume3 = new Costume();
            costume3.setName("黑色西装");
            costume3.setCostumeNo("COS003");
            costume3.setType(Costume.CostumeType.SUIT);
            costume3.setSize("XL");
            costume3.setColor("黑色");
            costume3.setUseCount(8);
            costume3.setCleaningStatus(Costume.CleaningStatus.TO_BE_CLEANED);
            costume3.setDescription("商务西装套装");
            costumeRepository.save(costume3);

            Costume costume4 = new Costume();
            costume4.setName("紫色晚礼服");
            costume4.setCostumeNo("COS004");
            costume4.setType(Costume.CostumeType.EVENING_DRESS);
            costume4.setSize("M");
            costume4.setColor("紫色");
            costume4.setUseCount(2);
            costume4.setCleaningStatus(Costume.CleaningStatus.CLEAN);
            costume4.setDescription("优雅紫色晚礼服");
            costumeRepository.save(costume4);

            Costume costume5 = new Costume();
            costume5.setName("汉服襦裙");
            costume5.setCostumeNo("COS005");
            costume5.setType(Costume.CostumeType.CHINESE_STYLE);
            costume5.setSize("S");
            costume5.setColor("粉色");
            costume5.setUseCount(4);
            costume5.setCleaningStatus(Costume.CleaningStatus.CLEANING);
            costume5.setDescription("唐代风格汉服");
            costume5.setAvailable(false);
            costumeRepository.save(costume5);
        }
    }

    private void initOrders() {
        if (orderRepository.count() == 0) {
            Order order1 = new Order();
            order1.setOrderNo("ORD20240515001");
            order1.setCustomerName("刘芳");
            order1.setCustomerPhone("13900139000");
            order1.setAddress("北京市朝阳区幸福小区1号楼101");
            order1.setAmount(new BigDecimal("5999.00"));
            order1.setStatus(Order.OrderStatus.COMPLETED);
            order1.setShootingDate(LocalDateTime.of(2024, 5, 10, 10, 0));
            order1.setCompletionDate(LocalDateTime.of(2024, 5, 15, 18, 0));
            orderRepository.save(order1);

            Order order2 = new Order();
            order2.setOrderNo("ORD20240516002");
            order2.setCustomerName("陈明");
            order2.setCustomerPhone("13900139001");
            order2.setAddress("北京市海淀区科技园区A座");
            order2.setAmount(new BigDecimal("8999.00"));
            order2.setStatus(Order.OrderStatus.DELIVERING);
            order2.setShootingDate(LocalDateTime.of(2024, 5, 12, 9, 0));
            orderRepository.save(order2);

            Order order3 = new Order();
            order3.setOrderNo("ORD20240517003");
            order3.setCustomerName("王丽");
            order3.setCustomerPhone("13900139002");
            order3.setAddress("北京市西城区金融街88号");
            order3.setAmount(new BigDecimal("6888.00"));
            order3.setStatus(Order.OrderStatus.EDITING);
            order3.setShootingDate(LocalDateTime.of(2024, 5, 14, 14, 0));
            orderRepository.save(order3);
        }
    }

    private void initSchedules() {
        if (scheduleRepository.count() == 0) {
            Employee photographer = employeeRepository.findByEmployeeNo("EMP001").orElse(null);
            Employee makeupArtist = employeeRepository.findByEmployeeNo("EMP002").orElse(null);
            
            if (photographer != null) {
                Schedule schedule1 = new Schedule();
                schedule1.setEmployee(photographer);
                schedule1.setScheduleDate(LocalDate.now().plusDays(1));
                schedule1.setStartTime(LocalTime.of(9, 0));
                schedule1.setEndTime(LocalTime.of(12, 0));
                schedule1.setCustomerName("刘芳");
                schedule1.setCustomerPhone("13900139000");
                schedule1.setShootingTheme("婚纱照-海景");
                schedule1.setStatus(Schedule.ScheduleStatus.CONFIRMED);
                scheduleRepository.save(schedule1);

                Schedule schedule2 = new Schedule();
                schedule2.setEmployee(photographer);
                schedule2.setScheduleDate(LocalDate.now().plusDays(1));
                schedule2.setStartTime(LocalTime.of(14, 0));
                schedule2.setEndTime(LocalTime.of(17, 0));
                schedule2.setCustomerName("陈明");
                schedule2.setCustomerPhone("13900139001");
                schedule2.setShootingTheme("个人写真");
                schedule2.setStatus(Schedule.ScheduleStatus.CONFIRMED);
                scheduleRepository.save(schedule2);
            }

            if (makeupArtist != null) {
                Schedule schedule3 = new Schedule();
                schedule3.setEmployee(makeupArtist);
                schedule3.setScheduleDate(LocalDate.now().plusDays(1));
                schedule3.setStartTime(LocalTime.of(8, 30));
                schedule3.setEndTime(LocalTime.of(12, 30));
                schedule3.setCustomerName("刘芳");
                schedule3.setCustomerPhone("13900139000");
                schedule3.setShootingTheme("婚纱化妆");
                schedule3.setStatus(Schedule.ScheduleStatus.CONFIRMED);
                scheduleRepository.save(schedule3);
            }
        }
    }

    private void initVenueBookings() {
        if (venueBookingRepository.count() == 0) {
            Venue venue1 = venueRepository.findByVenueNo("VEN001");
            Venue venue2 = venueRepository.findByVenueNo("VEN003");
            
            if (venue1 != null) {
                VenueBooking booking1 = new VenueBooking();
                booking1.setVenue(venue1);
                booking1.setBookingDate(LocalDate.now().plusDays(1));
                booking1.setStartTime(LocalTime.of(9, 0));
                booking1.setEndTime(LocalTime.of(12, 0));
                booking1.setCustomerName("刘芳");
                booking1.setCustomerPhone("13900139000");
                booking1.setShootingTheme("婚纱照-海景");
                booking1.setStatus(VenueBooking.BookingStatus.CONFIRMED);
                venueBookingRepository.save(booking1);
            }

            if (venue2 != null) {
                VenueBooking booking2 = new VenueBooking();
                booking2.setVenue(venue2);
                booking2.setBookingDate(LocalDate.now().plusDays(2));
                booking2.setStartTime(LocalTime.of(10, 0));
                booking2.setEndTime(LocalTime.of(16, 0));
                booking2.setCustomerName("陈明");
                booking2.setCustomerPhone("13900139001");
                booking2.setShootingTheme("外景写真");
                booking2.setStatus(VenueBooking.BookingStatus.CONFIRMED);
                venueBookingRepository.save(booking2);
            }
        }
    }

    private void initExpress() {
        if (expressRepository.count() == 0) {
            Order order1 = orderRepository.findByOrderNo("ORD20240516002").orElse(null);
            
            if (order1 != null) {
                Express express = new Express();
                express.setOrder(order1);
                express.setTrackingNo("SF1234567890");
                express.setCompany(Express.LogisticsCompany.SF_EXPRESS);
                express.setReceiverName("陈明");
                express.setReceiverPhone("13900139001");
                express.setReceiverAddress("北京市海淀区科技园区A座");
                express.setStatus(Express.ExpressStatus.IN_TRANSIT);
                express.setTrackingInfo(LocalDateTime.now() + " 包裹已揽收\n" + 
                                      LocalDateTime.now() + " 正在运输中");
                express.setShipTime(LocalDateTime.now().minusHours(2));
                expressRepository.save(express);
            }
        }
    }
}
