package schwarz.jobs.interview.coupon.core.services;

import java.util.List;
import java.util.Optional;

import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.services.model.Basket;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;

/**
 * A service interface that provides methods
 * to update the basket and create coupons as per the request
 * 
 * @author manik sharma
 */
public interface ICouponService {

	 public Optional<Basket> apply(final Basket basket, final String code);
	 
	 public Coupon createCoupon(final CouponDTO couponDTO);
	 
	 public List<CouponDTO> getCoupons();
	 
	 public CouponDTO findCouponByCode(String couponCode);
}
