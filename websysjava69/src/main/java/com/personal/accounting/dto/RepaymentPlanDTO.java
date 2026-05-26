package com.personal.accounting.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RepaymentPlanDTO {
    private Integer period;
    private LocalDate paymentDate;
    private BigDecimal paymentAmount;
    private BigDecimal principalPart;
    private BigDecimal interestPart;
    private BigDecimal remainingPrincipal;
}
