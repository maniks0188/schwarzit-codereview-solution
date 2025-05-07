package schwarz.jobs.interview.coupon.core.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.services.CouponService;
import schwarz.jobs.interview.coupon.core.services.model.Basket;
import schwarz.jobs.interview.coupon.web.CouponResource;
import schwarz.jobs.interview.coupon.web.dto.ApplicationRequestDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;

@ExtendWith(SpringExtension.class)
public class CouponResourceTest {

	@InjectMocks
	CouponResource controller;
	
	@Mock
	CouponService couponService;
	
	Coupon coupon = null;
	
	@BeforeEach
	void setup() {
		coupon = Coupon.builder()
	            .code("1111")
	            .discount(BigDecimal.TEN)
	            .minBasketValue(BigDecimal.valueOf(50))
	            .build();
	}
	
	@Test
	void test_valid_input_create_coupon() {
		CouponDTO request = new CouponDTO(BigDecimal.TEN, "1234", BigDecimal.valueOf(110));
		
		when(couponService.createCoupon(request)).thenReturn(coupon);
		ResponseEntity<CouponDTO> response = controller.create(request);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("1234", response.getBody().getCode());
		
	}
	
	@Test
	void test_valid_input_apply() {
		Basket basket = Basket.builder()
		        .value(BigDecimal.TEN)
		        .applicationSuccessful(true)
		        .build();
		ApplicationRequestDTO request = new ApplicationRequestDTO("1234", basket);
		
		when(couponService.apply(basket, request.getCode())).thenReturn(Optional.of(basket));
		
		ResponseEntity<Basket> response = controller.apply(request);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	@Test
	void test_invalid_input_apply() {
		Basket basket = Basket.builder()
		        .value(BigDecimal.TEN)
		        .applicationSuccessful(false)
		        .build();
		ApplicationRequestDTO request = new ApplicationRequestDTO("1234", basket);
		
		when(couponService.apply(basket, request.getCode())).thenReturn(Optional.of(basket));
		
		ResponseEntity<Basket> response = controller.apply(request);
		
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
		
	}
	
	@Test
	void test_getCoupons() {
		List<String> codes = Arrays.asList("1234");
		
		CouponDTO dto = new CouponDTO(BigDecimal.TEN, "1234", BigDecimal.valueOf(110));
		List<CouponDTO> lst = Arrays.asList(dto);
		when(couponService.getCoupons(codes)).thenReturn(lst);
		
		ResponseEntity<List<CouponDTO>> response = controller.getCoupons(codes);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, response.getBody().size());
		
	}
}
