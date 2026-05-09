package com.agricultural.service;

import com.agricultural.dto.DashboardDTO;
import org.springframework.stereotype.Service;

/**
 * 仪表盘服务类
 * 提供首页展示的汇总数据
 */
@Service
public class DashboardService {

    private final OrderService orderService;
    private final FinanceService financeService;
    private final CostService costService;

    public DashboardService(OrderService orderService,
                            FinanceService financeService,
                            CostService costService) {
        this.orderService = orderService;
        this.financeService = financeService;
        this.costService = costService;
    }

    /**
     * 获取仪表盘数据
     */
    public DashboardDTO getDashboardData() {
        DashboardDTO dto = new DashboardDTO();
        dto.setTotalSalesAmount(orderService.getTotalSalesAmount());
        dto.setTotalPurchaseAmount(orderService.getTotalPurchaseAmount());
        dto.setTotalReceivable(financeService.getTotalReceivable());
        dto.setTotalPayable(financeService.getTotalPayable());
        dto.setPendingOrdersCount(orderService.countPendingOrders());
        dto.setUnsettledFinanceCount(financeService.countUnsettled());
        dto.setCostSummary(costService.getCostSummary());
        return dto;
    }
}
