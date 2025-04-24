package schwarz.jobs.interview.coupon.web.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CouponDTO {

    private BigDecimal discount;

    private String code;

    private BigDecimal minBasketValue;

}
