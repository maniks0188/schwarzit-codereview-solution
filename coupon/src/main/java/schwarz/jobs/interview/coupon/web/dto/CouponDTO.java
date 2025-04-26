package schwarz.jobs.interview.coupon.web.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponDTO {

	@NotNull
    private BigDecimal discount;

    @NotNull
    private String code;

    @NotNull
    private BigDecimal minBasketValue;

}
