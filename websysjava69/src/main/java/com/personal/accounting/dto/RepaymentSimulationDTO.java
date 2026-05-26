package com.personal.accounting.dto;

import com.personal.accounting.enums.RepaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class RepaymentSimulationDTO {
    @NotNull(message = "贷款本金不能为空")
    @DecimalMin(value = "0.01", message = "贷款本金必须大于0")
    private BigDecimal principal;

    @NotNull(message = "年利率不能为空")
    @DecimalMin(value = "0", message = "年利率不能小于0")
    private BigDecimal annualInterestRate;

    @NotNull(message = "贷款期限不能为空")
    private Integer loanTermMonths;

    @NotNull(message = "还款方式不能为空")
    private RepaymentMethod repaymentMethod;

    private BigDecimal totalPayment;
    private BigDecimal totalInterest;
    private List<RepaymentPlanDTO> repaymentPlans;
}
