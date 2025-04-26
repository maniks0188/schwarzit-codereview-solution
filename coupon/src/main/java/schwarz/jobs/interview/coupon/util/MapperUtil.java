package schwarz.jobs.interview.coupon.util;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;

@AllArgsConstructor
@Component
public class MapperUtil {

	public CouponDTO convertToDto(Coupon coupon) {
		CouponDTO dto = new CouponDTO();
		dto.setCode(coupon.getCode());
		dto.setDiscount(coupon.getDiscount());
		dto.setMinBasketValue(coupon.getMinBasketValue());
		
		return dto;
	}
}
