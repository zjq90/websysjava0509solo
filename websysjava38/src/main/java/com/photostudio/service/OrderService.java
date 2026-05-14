package com.photostudio.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.photostudio.common.Result;
import com.photostudio.entity.*;
import com.photostudio.entity.Package;
import com.photostudio.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 订单服务类
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private AddOnItemRepository addOnItemRepository;

    @Autowired
    private AlbumRepository albumRepository;

    /**
     * 订单阶段映射
     */
    private static final Map<Integer, String> STAGE_MAP = new HashMap<>();
    static {
        STAGE_MAP.put(0, "待付定金");
        STAGE_MAP.put(1, "已付定金");
        STAGE_MAP.put(2, "拍摄中");
        STAGE_MAP.put(3, "选片中");
        STAGE_MAP.put(4, "修片中");
        STAGE_MAP.put(5, "产品制作中");
        STAGE_MAP.put(6, "已完成");
    }

    /**
     * 生成订单编号
     */
    private String generateOrderNo() {
        String prefix = "PS" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String suffix = String.format("%04d", new Random().nextInt(10000));
        return prefix + suffix;
    }

    /**
     * 查询所有订单
     */
    public Result<List<Order>> findAll() {
        List<Order> list = orderRepository.findAll();
        return Result.success(list);
    }

    /**
     * 分页查询订单
     */
    public Result<Page<Order>> findPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Order> result = orderRepository.findAll(pageable);
        return Result.success(result);
    }

    /**
     * 根据ID查询订单
     */
    public Result<Order> findById(Long id) {
        Optional<Order> optional = orderRepository.findById(id);
        return optional.map(Result::success).orElse(Result.error("订单不存在"));
    }

    /**
     * 根据订单编号查询
     */
    public Result<Order> findByOrderNo(String orderNo) {
        Optional<Order> optional = orderRepository.findByOrderNo(orderNo);
        return optional.map(Result::success).orElse(Result.error("订单不存在"));
    }

    /**
     * 创建订单
     */
    @Transactional
    public Result<Order> createOrder(Long customerId, Long packageId, List<Long> addOnItemIds, 
                                     String channelSource, String shootingLocation, LocalDateTime shootingDate,
                                     String remark) {
        // 验证客户
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (!customerOpt.isPresent()) {
            return Result.error("客户不存在");
        }
        Customer customer = customerOpt.get();

        // 验证套餐
        Optional<Package> packageOpt = packageRepository.findById(packageId);
        if (!packageOpt.isPresent()) {
            return Result.error("套餐不存在");
        }
        Package pkg = packageOpt.get();

        // 计算加购项总价
        BigDecimal addOnTotal = BigDecimal.ZERO;
        JSONArray addOnItemsJson = new JSONArray();
        if (addOnItemIds != null && !addOnItemIds.isEmpty()) {
            for (Long itemId : addOnItemIds) {
                Optional<AddOnItem> itemOpt = addOnItemRepository.findById(itemId);
                if (itemOpt.isPresent()) {
                    AddOnItem item = itemOpt.get();
                    addOnTotal = addOnTotal.add(item.getPrice());
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("id", item.getId());
                    itemMap.put("name", item.getName());
                    itemMap.put("price", item.getPrice());
                    itemMap.put("category", item.getCategory());
                    addOnItemsJson.add(itemMap);
                }
            }
        }

        // 计算订单总金额
        BigDecimal totalAmount = pkg.getPrice().add(addOnTotal);
        BigDecimal depositAmount = totalAmount.multiply(new BigDecimal("0.3"));

        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setCustomer(customer);
        order.setCustomerName(customer.getName());
        order.setCustomerPhone(customer.getPhone());
        order.setPackageInfo(pkg);
        order.setPackageName(pkg.getName());
        order.setPackagePrice(pkg.getPrice());
        order.setAddOnItemsJson(addOnItemsJson.toJSONString());
        order.setAddOnTotal(addOnTotal);
        order.setTotalAmount(totalAmount);
        order.setDepositAmount(depositAmount);
        order.setChannelSource(channelSource != null ? channelSource : "门店");
        order.setShootingLocation(shootingLocation);
        order.setShootingDate(shootingDate);
        order.setRemark(remark);
        order.setCurrentStage(0);
        order.setStatus("待付定金");

        Order savedOrder = orderRepository.save(order);

        // 创建云相册
        Album album = new Album();
        album.setAlbumNo("ALB" + System.currentTimeMillis());
        album.setName(customer.getName() + "的相册");
        album.setOrderId(savedOrder.getId());
        album.setCustomerId(customerId);
        album.setAccessCode(String.format("%06d", new Random().nextInt(1000000)));
        albumRepository.save(album);

        // 更新订单相册ID
        savedOrder.setAlbumId(album.getId());
        orderRepository.save(savedOrder);

        return Result.success("订单创建成功", savedOrder);
    }

    /**
     * 更新订单状态/阶段
     */
    @Transactional
    public Result<Order> updateStage(Long orderId, Integer stage, String operator) {
        Optional<Order> optional = orderRepository.findById(orderId);
        if (!optional.isPresent()) {
            return Result.error("订单不存在");
        }
        Order order = optional.get();
        
        if (stage < 0 || stage > 6) {
            return Result.error("无效的阶段");
        }

        order.setCurrentStage(stage);
        order.setStatus(STAGE_MAP.get(stage));

        // 更新阶段时间
        LocalDateTime now = LocalDateTime.now();
        switch (stage) {
            case 1: order.setStage1Time(now); break;
            case 2: order.setStage2Time(now); break;
            case 3: order.setStage3Time(now); break;
            case 4: order.setStage4Time(now); break;
            case 5: order.setStage5Time(now); break;
            case 6: order.setStage6Time(now); break;
        }

        Order saved = orderRepository.save(order);
        return Result.success("订单阶段更新成功", saved);
    }

    /**
     * 支付定金
     */
    @Transactional
    public Result<Order> payDeposit(Long orderId) {
        Optional<Order> optional = orderRepository.findById(orderId);
        if (!optional.isPresent()) {
            return Result.error("订单不存在");
        }
        Order order = optional.get();
        
        if (order.getCurrentStage() >= 1) {
            return Result.error("定金已支付");
        }

        order.setPaidAmount(order.getDepositAmount());
        return updateStage(orderId, 1, "系统");
    }

    /**
     * 签署电子合同
     */
    @Transactional
    public Result<Order> signContract(Long orderId, String signIp) {
        Optional<Order> optional = orderRepository.findById(orderId);
        if (!optional.isPresent()) {
            return Result.error("订单不存在");
        }
        Order order = optional.get();
        
        order.setContractSigned(1);
        order.setContractSignedTime(LocalDateTime.now());
        order.setContractSignIp(signIp);
        
        Order saved = orderRepository.save(order);
        return Result.success("合同签署成功", saved);
    }

    /**
     * 分配摄影师/化妆师/修图师
     */
    @Transactional
    public Result<Order> assignEmployee(Long orderId, String photographer, String makeupArtist, String retoucher) {
        Optional<Order> optional = orderRepository.findById(orderId);
        if (!optional.isPresent()) {
            return Result.error("订单不存在");
        }
        Order order = optional.get();
        
        if (photographer != null) {
            order.setPhotographer(photographer);
        }
        if (makeupArtist != null) {
            order.setMakeupArtist(makeupArtist);
        }
        if (retoucher != null) {
            order.setRetoucher(retoucher);
        }
        
        Order saved = orderRepository.save(order);
        return Result.success("人员分配成功", saved);
    }

    /**
     * 根据客户ID查询订单
     */
    public Result<List<Order>> findByCustomerId(Long customerId) {
        List<Order> list = orderRepository.findByCustomerIdOrderByCreateTimeDesc(customerId);
        return Result.success(list);
    }

    /**
     * 根据手机号查询客户订单
     */
    public Result<List<Order>> findByCustomerPhone(String phone) {
        List<Order> list = orderRepository.findByCustomerPhoneOrderByCreateTimeDesc(phone);
        return Result.success(list);
    }

    /**
     * 根据阶段查询订单
     */
    public Result<List<Order>> findByStage(Integer stage) {
        List<Order> list = orderRepository.findByCurrentStageOrderByCreateTimeDesc(stage);
        return Result.success(list);
    }

    /**
     * 获取订单统计数据
     */
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("todayOrders", orderRepository.countTodayOrders());
        stats.put("monthAmount", orderRepository.sumMonthTotalAmount());
        stats.put("totalOrders", orderRepository.count());
        
        // 各阶段订单数量
        Map<Integer, Long> stageCounts = new HashMap<>();
        for (int i = 0; i <= 6; i++) {
            List<Order> list = orderRepository.findByCurrentStageOrderByCreateTimeDesc(i);
            stageCounts.put(i, (long) list.size());
        }
        stats.put("stageCounts", stageCounts);
        
        return Result.success(stats);
    }

    /**
     * 更新订单
     */
    public Result<Order> update(Order order) {
        if (order.getId() == null) {
            return Result.error("订单ID不能为空");
        }
        Order saved = orderRepository.save(order);
        return Result.success("订单更新成功", saved);
    }

    /**
     * 删除订单
     */
    public Result<Void> delete(Long id) {
        if (!orderRepository.existsById(id)) {
            return Result.error("订单不存在");
        }
        orderRepository.deleteById(id);
        return Result.success("订单删除成功", null);
    }
}
