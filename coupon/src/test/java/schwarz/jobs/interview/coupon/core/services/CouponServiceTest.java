package schwarz.jobs.interview.coupon.core.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.repository.CouponRepository;
import schwarz.jobs.interview.coupon.core.services.model.Basket;
import schwarz.jobs.interview.coupon.exception.InvalidBasketException;
import schwarz.jobs.interview.coupon.util.Constants;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;

@ExtendWith(SpringExtension.class)
public class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Test
    public void createCoupon() {
        CouponDTO dto = CouponDTO.builder()
            .code("12345")
            .discount(BigDecimal.TEN)
            .minBasketValue(BigDecimal.valueOf(50))
            .build();

        Coupon coupon = new Coupon(Long.MIN_VALUE,"12345", BigDecimal.TEN, BigDecimal.valueOf(50));
        when(couponRepository.save(Mockito.any(Coupon.class))).thenReturn(coupon);
        
        Coupon savedCoupon = couponService.createCoupon(dto);

        verify(couponRepository, times(1)).save(any());
        assertEquals(savedCoupon.getCode(), coupon.getCode());
    }
    
    @Test
    public void createCoupon_validation_failed() {
        CouponDTO dto = CouponDTO.builder()
            .code("12345")
            .discount(null)
            .minBasketValue(BigDecimal.valueOf(50))
            .build();

        assertThatThrownBy(() -> {
            couponService.createCoupon(dto);
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Constants.INVALID_DISCOUNT_VALUE);
    }

    @Test
    public void test_apply_coupon_method_true() {

        final Basket basket = createBasket(BigDecimal.valueOf(100));

        when(couponRepository.findByCode("1111")).thenReturn(Optional.of(Coupon.builder()
            .code("1111")
            .discount(BigDecimal.TEN)
            .minBasketValue(BigDecimal.valueOf(50))
            .build()));

        Optional<Basket> optionalBasket = couponService.apply(basket, "1111");

        assertThat(optionalBasket).hasValueSatisfying(b -> {
            assertThat(b.getAppliedDiscount()).isEqualTo(BigDecimal.TEN);
            assertThat(b.isApplicationSuccessful()).isTrue();
        });
    }

    @Test
    public void test_apply_coupon_method_false() {
    	 final Basket basket = createBasket(BigDecimal.valueOf(0));

    	 when(couponRepository.findByCode("1111")).thenReturn(Optional.of(Coupon.builder()
 	            .code("1111")
 	            .discount(BigDecimal.TEN)
 	            .minBasketValue(BigDecimal.valueOf(50))
 	            .build()));
 	 
    	 Optional<Basket> optionalBasket = couponService.apply(basket, "1111");

    	        assertThat(optionalBasket).hasValueSatisfying(b -> {
    	            assertThat(b).isEqualTo(basket);
    	            assertThat(b.isApplicationSuccessful()).isFalse();
    	        });
    }
    
    @Test
    public void test_apply_coupon_method_failure() {
    	final Basket basket = createBasket(BigDecimal.valueOf(-1));
    	
    	 when(couponRepository.findByCode("1111")).thenReturn(Optional.of(Coupon.builder()
    	            .code("1111")
    	            .discount(BigDecimal.TEN)
    	            .minBasketValue(BigDecimal.valueOf(50))
    	            .build()));
    	 
            assertThatThrownBy(() -> {
                couponService.apply(basket, "1111");
            }).isInstanceOf(InvalidBasketException.class)
                .hasMessage(Constants.INVALID_BASKET_VALUE);
    }
    
    @Test
    public void should_test_get_Coupons() {

		/*
		 * CouponRequestDTO dto = CouponRequestDTO.builder()
		 * .codes(Arrays.asList("1111", "1234")) .build();
		 */
        List<String> codes = Arrays.asList("1111", "1234");
        
      when(couponRepository.findByCode(any()))
            .thenReturn(Optional.of(Coupon.builder()
                .code("1111")
                .discount(BigDecimal.TEN)
                .minBasketValue(BigDecimal.valueOf(50))
                .build()))
            .thenReturn(Optional.of(Coupon.builder()
                .code("1234")
                .discount(BigDecimal.TEN)
                .minBasketValue(BigDecimal.valueOf(50))
                .build()));

        List<CouponDTO> returnedCoupons = couponService.getCoupons(codes);

        assertThat(returnedCoupons.get(0).getCode()).isEqualTo("1111");

        assertThat(returnedCoupons.get(1).getCode()).isEqualTo("1234");
    }
    
    @Test
    public void should_test_get_Coupons_not_present() {
    	List<String> codes = Arrays.asList("999");
    	
    	 when(couponRepository.findByCode(any()))
         .thenReturn(Optional.empty());

     List<CouponDTO> returnedCoupons = couponService.getCoupons(codes);
     
     assertEquals(0, returnedCoupons.size());
    }
    
    private Basket createBasket(BigDecimal value) {
    	return Basket.builder()
        .value(value)
        .build();
    }
    
}
