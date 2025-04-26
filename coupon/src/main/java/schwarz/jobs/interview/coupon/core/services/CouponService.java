package schwarz.jobs.interview.coupon.core.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.repository.CouponRepository;
import schwarz.jobs.interview.coupon.core.services.model.Basket;
import schwarz.jobs.interview.coupon.exception.CouponNotFoundException;
import schwarz.jobs.interview.coupon.exception.InvalidBasketException;
import schwarz.jobs.interview.coupon.util.Constants;
import schwarz.jobs.interview.coupon.util.MapperUtil;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;

/**
 * Implementation of {@link ICouponService} that provides methods
 * to update basket and create coupons
 * 
 * @author manik sharma
 */
@Service
@RequiredArgsConstructor
public class CouponService implements ICouponService{

	private static final Logger log = LoggerFactory.getLogger(CouponService.class);

    private final CouponRepository couponRepository;
    
    private final MapperUtil util;

    /**
     * Finds the coupon object by code
     * 
     * @param coupon code
     * @return Optional of {@link Coupon}
     * 
     */
	public Optional<Coupon> getCoupon(final String code) {
		return couponRepository.findByCode(code);
	} 

	/**
     * <p>This method checks the basket value and 
     *    if the basket value is > 0 then it applies the discount from the coupon
     *    else it returns an exception of invalid basket value.
     * </p> 
     * 
     * @param {@link Basket} and coupon code
     * @return Optional of {@link Basket}
     * 
     * @throws InvalidBasketException if the basket value is negative
     * 
     */
    public Optional<Basket> apply(final Basket basket, final String code) {

    	return getCoupon(code).map(coupon -> {
    		double basketValue = basket.getValue().doubleValue();
    		if(basketValue < 0) {
    			log.error(Constants.INVALID_BASKET_VALUE);
    			throw new InvalidBasketException(Constants.INVALID_BASKET_VALUE);
    		}else if(basketValue > 0) {
    			log.info(Constants.COUPON_APPLIED_SUCCESS);
    			basket.applyDiscount(coupon.getDiscount());
    		}
    		
    		return basket;
    	});
//        return getCoupon(code).map(coupon -> {
//
//            if (basket.getValue().doubleValue() >= 0) {
//
//                if (basket.getValue().doubleValue() > 0) {
//
//                    basket.applyDiscount(coupon.getDiscount());
//
//                } else if (basket.getValue().doubleValue() == 0) {
//                    return basket;
//                }
//
//            } else {
//                System.out.println("DEBUG: TRIED TO APPLY NEGATIVE DISCOUNT!");
//                throw new RuntimeException("Can't apply negative discounts");
//            }
//
//            return basket;
//        });
    }

    /**
     * <p>This method checks the basket value and 
     *    if the basket value is > 0 then it applies the discount from the coupon
     *    else it returns an exception of invalid basket value.
     * </p> 
     * 
     * @param {@link Basket} and coupon code
     * @return Optional of {@link Basket}
     * 
     * @throws InvalidBasketException if the basket value is negative
     * 
     */
	public Coupon createCoupon(final CouponDTO couponDTO) {

		Coupon coupon = null;
		log.info("Creating coupon...");
		validateInputs(couponDTO);
		coupon = Coupon.builder().code(couponDTO.getCode().toLowerCase()).discount(couponDTO.getDiscount())
				.minBasketValue(couponDTO.getMinBasketValue()).build();

		return couponRepository.save(coupon);
	}

    /**
     * This method validates the input provided by the user
     * 
     * @param couponDTO
     * @throws IllegalArgumentException
     */
    private void validateInputs(CouponDTO couponDTO) {
    	
    	if(couponDTO.getDiscount() == null || couponDTO.getDiscount().doubleValue() < 0) {
    		log.error("Invalid Inputs: Discount Value cannot be null or negative.");
    		throw new IllegalArgumentException(Constants.INVALID_DISCOUNT_VALUE);
    	}
    	if(couponDTO.getCode() == null || couponDTO.getCode().isBlank()) {
    		log.error("Invalid Inputs: Coupon code cannot be null or blank.");
    		throw new IllegalArgumentException(Constants.INVALID_COUPON_CODE);
    	}
    	if(couponDTO.getMinBasketValue() == null || couponDTO.getMinBasketValue().doubleValue() < 0) {
    		log.error("Invalid Inputs: Basket Value cannot be null or negative.");
    		throw new IllegalArgumentException(Constants.INVALID_BASKET_VALUE);
    	}
    	
    }
	/*
	 * public List<Coupon> getCoupons(final CouponRequestDTO couponRequestDTO) {
	 * 
	 * final ArrayList<Coupon> foundCoupons = new ArrayList<>();
	 * 
	 * couponRequestDTO.getCodes().forEach(code ->
	 * foundCoupons.add(couponRepository.findByCode(code).get()));
	 * 
	 * return foundCoupons; }
	 */

    /**
     * This method returns all the coupons from the database
     * 
     * @return {@link Optional of CouponDTO}
     */
    public List<CouponDTO> getCoupons() {
    	log.info("Calling get all coupons!");
    	List<Coupon> dbList = couponRepository.findAll();
    	return dbList.stream().map(coupon -> new CouponDTO(coupon.getDiscount(), coupon.getCode(), coupon.getMinBasketValue())).collect(Collectors.toList());
    }
    
    /**
     * This method returns the coupon based on code from the database
     * @param String couponCode
     * @return {@link Optional of CouponDTO}
     */
    public CouponDTO findCouponByCode(String couponCode) {

    	log.info("Calling getCoupon using code!");
    	Optional<Coupon> coupon = getCoupon(couponCode);
    	if(coupon.isPresent()) {
    		return util.convertToDto(coupon.get());
    	}
    	log.info("No coupon found for code {}!", couponCode);
    	throw new CouponNotFoundException(Constants.INVALID_COUPON);
    }
}
