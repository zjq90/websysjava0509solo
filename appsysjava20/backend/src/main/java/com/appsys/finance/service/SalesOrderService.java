package com.appsys.finance.service;

import com.appsys.finance.entity.SalesOrder;
import com.appsys.finance.entity.Employee;
import com.appsys.finance.entity.Product;
import com.appsys.finance.entity.Customer;
import com.appsys.finance.repository.SalesOrderRepository;
import com.appsys.finance.repository.EmployeeRepository;
import com.appsys.finance.repository.ProductRepository;
import com.appsys.finance.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SalesOrderService {

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public SalesOrder createOrder(SalesOrder order) {
        order.setOrderNo("ORD" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase());
        
        BigDecimal totalAmount = order.getUnitPrice().multiply(new BigDecimal(order.getQuantity()));
        order.setTotalAmount(totalAmount);

        if (order.getPaidAmount() == null) {
            order.setPaidAmount(BigDecimal.ZERO);
        }

        if (order.getEmployee() != null && order.getEmployee().getId() != null) {
            Employee employee = employeeRepository.findById(order.getEmployee().getId())
                .orElseThrow(() -> new RuntimeException("员工不存在"));
            if (employee.getCommissionRate() != null) {
                BigDecimal commissionRate = employee.getCommissionRate().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
                BigDecimal commissionAmount = order.getPaidAmount().multiply(commissionRate)
                    .setScale(2, RoundingMode.HALF_UP);
                order.setCommissionAmount(commissionAmount);
            }
        }

        return salesOrderRepository.save(order);
    }

    public Optional<SalesOrder> getOrderById(Long id) {
        return salesOrderRepository.findById(id);
    }

    public Optional<SalesOrder> getOrderByOrderNo(String orderNo) {
        return salesOrderRepository.findByOrderNo(orderNo);
    }

    public List<SalesOrder> getOrdersByEmployeeId(Long employeeId) {
        return salesOrderRepository.findByEmployeeId(employeeId);
    }

    public List<SalesOrder> getAllOrders() {
        return salesOrderRepository.findAll();
    }

    @Transactional
    public SalesOrder updateOrder(Long id, SalesOrder orderDetails) {
        return salesOrderRepository.findById(id).map(order -> {
            if (orderDetails.getCustomer() != null) {
                order.setCustomer(orderDetails.getCustomer());
            }
            if (orderDetails.getProduct() != null) {
                order.setProduct(orderDetails.getProduct());
            }
            if (orderDetails.getEmployee() != null) {
                order.setEmployee(orderDetails.getEmployee());
            }
            if (orderDetails.getQuantity() != null) {
                order.setQuantity(orderDetails.getQuantity());
            }
            if (orderDetails.getUnitPrice() != null) {
                order.setUnitPrice(orderDetails.getUnitPrice());
            }
            if (order.getQuantity() != null && order.getUnitPrice() != null) {
                BigDecimal totalAmount = order.getUnitPrice().multiply(new BigDecimal(order.getQuantity()));
                order.setTotalAmount(totalAmount);
            }
            if (orderDetails.getPaidAmount() != null) {
                order.setPaidAmount(orderDetails.getPaidAmount());
            }
            if (orderDetails.getStatus() != null) {
                order.setStatus(orderDetails.getStatus());
            }
            if (orderDetails.getRemark() != null) {
                order.setRemark(orderDetails.getRemark());
            }

            if (order.getEmployee() != null && order.getEmployee().getId() != null && order.getPaidAmount() != null) {
                Employee employee = employeeRepository.findById(order.getEmployee().getId())
                    .orElseThrow(() -> new RuntimeException("员工不存在"));
                if (employee.getCommissionRate() != null) {
                    BigDecimal commissionRate = employee.getCommissionRate().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
                    BigDecimal commissionAmount = order.getPaidAmount().multiply(commissionRate)
                        .setScale(2, RoundingMode.HALF_UP);
                    order.setCommissionAmount(commissionAmount);
                }
            }

            return salesOrderRepository.save(order);
        }).orElseThrow(() -> new RuntimeException("订单不存在，ID: " + id));
    }

    public void deleteOrder(Long id) {
        salesOrderRepository.deleteById(id);
    }

    public BigDecimal getPersonalSalesPerformance(Long employeeId, int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDateTime startDate = ym.atDay(1).atStartOfDay();
        LocalDateTime endDate = ym.atEndOfMonth().atTime(23, 59, 59);
        return salesOrderRepository.sumTotalAmountByEmployeeAndDateRange(employeeId, startDate, endDate);
    }

    public BigDecimal getPersonalReceivedAmount(Long employeeId, int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDateTime startDate = ym.atDay(1).atStartOfDay();
        LocalDateTime endDate = ym.atEndOfMonth().atTime(23, 59, 59);
        return salesOrderRepository.sumPaidAmountByEmployeeAndDateRange(employeeId, startDate, endDate);
    }

    public BigDecimal getPersonalCommission(Long employeeId, int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDateTime startDate = ym.atDay(1).atStartOfDay();
        LocalDateTime endDate = ym.atEndOfMonth().atTime(23, 59, 59);
        return salesOrderRepository.sumCommissionAmountByEmployeeAndDateRange(employeeId, startDate, endDate);
    }

    public BigDecimal getTotalSalesAmount(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDateTime startDate = ym.atDay(1).atStartOfDay();
        LocalDateTime endDate = ym.atEndOfMonth().atTime(23, 59, 59);
        return salesOrderRepository.sumTotalAmountByDateRange(startDate, endDate);
    }
}
