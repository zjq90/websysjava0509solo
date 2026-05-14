package com.photostudio.config;

import com.photostudio.entity.*;
import com.photostudio.entity.Package;
import com.photostudio.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 数据初始化器
 * 应用启动时自动生成测试数据
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private AddOnItemRepository addOnItemRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) throws Exception {
        // 初始化套餐数据
        initPackages();
        // 初始化加购项数据
        initAddOnItems();
        // 初始化员工数据
        initEmployees();
        // 初始化客户数据
        initCustomers();
        
        System.out.println("========================================");
        System.out.println("  测试数据初始化完成!");
        System.out.println("========================================");
    }

    private void initPackages() {
        if (packageRepository.count() == 0) {
            Package pkg1 = new Package();
            pkg1.setName("轻奢婚纱套餐");
            pkg1.setType("婚纱");
            pkg1.setPrice(new BigDecimal("5999.00"));
            pkg1.setDescription("包含3套服装、100张底片、40张精修、1本相册、3个相框、1个海报");
            pkg1.setPhotoCount(100);
            pkg1.setRetouchCount(40);
            pkg1.setAlbumCount(1);
            pkg1.setFrameCount(3);
            pkg1.setShootingDays(1);
            pkg1.setClothingSets(3);
            pkg1.setMakeupTimes(3);
            pkg1.setStatus(1);
            pkg1.setSortOrder(1);
            packageRepository.save(pkg1);

            Package pkg2 = new Package();
            pkg2.setName("豪华婚纱套餐");
            pkg2.setType("婚纱");
            pkg2.setPrice(new BigDecimal("9999.00"));
            pkg2.setDescription("包含5套服装、200张底片、80张精修、2本相册、5个相框、2个海报、外景拍摄");
            pkg2.setPhotoCount(200);
            pkg2.setRetouchCount(80);
            pkg2.setAlbumCount(2);
            pkg2.setFrameCount(5);
            pkg2.setShootingDays(2);
            pkg2.setClothingSets(5);
            pkg2.setMakeupTimes(5);
            pkg2.setStatus(1);
            pkg2.setSortOrder(2);
            packageRepository.save(pkg2);

            Package pkg3 = new Package();
            pkg3.setName("个人写真套餐");
            pkg3.setType("写真");
            pkg3.setPrice(new BigDecimal("1999.00"));
            pkg3.setDescription("包含2套服装、50张底片、20张精修、1本相册");
            pkg3.setPhotoCount(50);
            pkg3.setRetouchCount(20);
            pkg3.setAlbumCount(1);
            pkg3.setFrameCount(0);
            pkg3.setShootingDays(1);
            pkg3.setClothingSets(2);
            pkg3.setMakeupTimes(2);
            pkg3.setStatus(1);
            pkg3.setSortOrder(3);
            packageRepository.save(pkg3);

            Package pkg4 = new Package();
            pkg4.setName("全家福套餐");
            pkg4.setType("全家福");
            pkg4.setPrice(new BigDecimal("2999.00"));
            pkg4.setDescription("包含4人服装、80张底片、30张精修、1本相册、2个相框");
            pkg4.setPhotoCount(80);
            pkg4.setRetouchCount(30);
            pkg4.setAlbumCount(1);
            pkg4.setFrameCount(2);
            pkg4.setShootingDays(1);
            pkg4.setClothingSets(4);
            pkg4.setMakeupTimes(4);
            pkg4.setStatus(1);
            pkg4.setSortOrder(4);
            packageRepository.save(pkg4);

            Package pkg5 = new Package();
            pkg5.setName("儿童写真套餐");
            pkg5.setType("儿童");
            pkg5.setPrice(new BigDecimal("1599.00"));
            pkg5.setDescription("包含3套服装、60张底片、25张精修、1本相册");
            pkg5.setPhotoCount(60);
            pkg5.setRetouchCount(25);
            pkg5.setAlbumCount(1);
            pkg5.setFrameCount(0);
            pkg5.setShootingDays(1);
            pkg5.setClothingSets(3);
            pkg5.setMakeupTimes(3);
            pkg5.setStatus(1);
            pkg5.setSortOrder(5);
            packageRepository.save(pkg5);
        }
    }

    private void initAddOnItems() {
        if (addOnItemRepository.count() == 0) {
            AddOnItem item1 = new AddOnItem();
            item1.setName("加拍夜景");
            item1.setCategory("拍摄");
            item1.setPrice(new BigDecimal("500.00"));
            item1.setDescription("额外增加2小时夜景拍摄");
            item1.setUnit("次");
            item1.setStatus(1);
            addOnItemRepository.save(item1);

            AddOnItem item2 = new AddOnItem();
            item2.setName("精修照片");
            item2.setCategory("精修");
            item2.setPrice(new BigDecimal("50.00"));
            item2.setDescription("单张照片高级精修");
            item2.setUnit("张");
            item2.setStatus(1);
            addOnItemRepository.save(item2);

            AddOnItem item3 = new AddOnItem();
            item3.setName("外景拍摄");
            item3.setCategory("拍摄");
            item3.setPrice(new BigDecimal("1000.00"));
            item3.setDescription("增加1个外景拍摄地点");
            item3.setUnit("个");
            item3.setStatus(1);
            addOnItemRepository.save(item3);

            AddOnItem item4 = new AddOnItem();
            item4.setName("12寸水晶相册");
            item4.setCategory("产品");
            item4.setPrice(new BigDecimal("800.00"));
            item4.setDescription("12寸水晶相册1本");
            item4.setUnit("本");
            item4.setStatus(1);
            addOnItemRepository.save(item4);

            AddOnItem item5 = new AddOnItem();
            item5.setName("36寸相框");
            item5.setCategory("产品");
            item5.setPrice(new BigDecimal("600.00"));
            item5.setDescription("36寸精美相框1个");
            item5.setUnit("个");
            item5.setStatus(1);
            addOnItemRepository.save(item5);
        }
    }

    private void initEmployees() {
        if (employeeRepository.count() == 0) {
            Employee emp1 = new Employee();
            emp1.setName("李明");
            emp1.setPhone("13900139001");
            emp1.setEmail("liming@photostudio.com");
            emp1.setPosition("摄影师");
            emp1.setSpecialty("人像,婚纱");
            emp1.setLevel(3);
            emp1.setStatus(1);
            emp1.setTaskCount(0);
            emp1.setCompletedCount(156);
            emp1.setRating(new BigDecimal("4.8"));
            employeeRepository.save(emp1);

            Employee emp2 = new Employee();
            emp2.setName("王芳");
            emp2.setPhone("13900139002");
            emp2.setEmail("wangfang@photostudio.com");
            emp2.setPosition("化妆师");
            emp2.setSpecialty("新娘妆,时尚妆");
            emp2.setLevel(3);
            emp2.setStatus(1);
            emp2.setTaskCount(0);
            emp2.setCompletedCount(142);
            emp2.setRating(new BigDecimal("4.9"));
            employeeRepository.save(emp2);

            Employee emp3 = new Employee();
            emp3.setName("张伟");
            emp3.setPhone("13900139003");
            emp3.setEmail("zhangwei@photostudio.com");
            emp3.setPosition("修图师");
            emp3.setSpecialty("人像精修,调色");
            emp3.setLevel(2);
            emp3.setStatus(1);
            emp3.setTaskCount(0);
            emp3.setCompletedCount(286);
            emp3.setRating(new BigDecimal("4.7"));
            employeeRepository.save(emp3);

            Employee emp4 = new Employee();
            emp4.setName("陈静");
            emp4.setPhone("13900139004");
            emp4.setEmail("chenjing@photostudio.com");
            emp4.setPosition("设计师");
            emp4.setSpecialty("相册设计,排版");
            emp4.setLevel(2);
            emp4.setStatus(1);
            emp4.setTaskCount(0);
            emp4.setCompletedCount(203);
            emp4.setRating(new BigDecimal("4.8"));
            employeeRepository.save(emp4);

            Employee emp5 = new Employee();
            emp5.setName("刘洋");
            emp5.setPhone("13900139005");
            emp5.setEmail("liuyang@photostudio.com");
            emp5.setPosition("摄影师");
            emp5.setSpecialty("儿童,全家福");
            emp5.setLevel(2);
            emp5.setStatus(1);
            emp5.setTaskCount(0);
            emp5.setCompletedCount(98);
            emp5.setRating(new BigDecimal("4.6"));
            employeeRepository.save(emp5);
        }
    }

    private void initCustomers() {
        if (customerRepository.count() == 0) {
            Customer cust1 = new Customer();
            cust1.setName("张三");
            cust1.setPhone("13800138001");
            cust1.setEmail("zhangsan@example.com");
            cust1.setWechat("zhangsan_wx");
            cust1.setGender("男");
            cust1.setAge(28);
            cust1.setAddress("北京市朝阳区建国路88号");
            cust1.setSource("美团");
            cust1.setVipLevel(1);
            cust1.setTotalAmount(new BigDecimal("5999.00"));
            customerRepository.save(cust1);

            Customer cust2 = new Customer();
            cust2.setName("李四");
            cust2.setPhone("13800138002");
            cust2.setEmail("lisi@example.com");
            cust2.setWechat("lisi_wx");
            cust2.setGender("女");
            cust2.setAge(25);
            cust2.setAddress("北京市海淀区中关村大街1号");
            cust2.setSource("抖音");
            cust2.setVipLevel(0);
            cust2.setTotalAmount(new BigDecimal("1999.00"));
            customerRepository.save(cust2);

            Customer cust3 = new Customer();
            cust3.setName("王五");
            cust3.setPhone("13800138003");
            cust3.setEmail("wangwu@example.com");
            cust3.setWechat("wangwu_wx");
            cust3.setGender("男");
            cust3.setAge(35);
            cust3.setAddress("北京市西城区金融街");
            cust3.setSource("门店");
            cust3.setVipLevel(2);
            cust3.setTotalAmount(new BigDecimal("12999.00"));
            customerRepository.save(cust3);

            Customer cust4 = new Customer();
            cust4.setName("赵六");
            cust4.setPhone("13800138004");
            cust4.setEmail("zhaoliu@example.com");
            cust4.setWechat("zhaoliu_wx");
            cust4.setGender("女");
            cust4.setAge(30);
            cust4.setAddress("北京市东城区王府井大街");
            cust4.setSource("小程序");
            cust4.setVipLevel(1);
            cust4.setTotalAmount(new BigDecimal("2999.00"));
            customerRepository.save(cust4);

            Customer cust5 = new Customer();
            cust5.setName("孙七");
            cust5.setPhone("13800138005");
            cust5.setEmail("sunqi@example.com");
            cust5.setWechat("sunqi_wx");
            cust5.setGender("男");
            cust5.setAge(6);
            cust5.setAddress("北京市丰台区方庄");
            cust5.setSource("老客户推荐");
            cust5.setVipLevel(0);
            cust5.setTotalAmount(new BigDecimal("1599.00"));
            customerRepository.save(cust5);
        }
    }
}
