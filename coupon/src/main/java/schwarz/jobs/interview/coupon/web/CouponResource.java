package schwarz.jobs.interview.coupon.web;


import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.services.CouponService;
import schwarz.jobs.interview.coupon.core.services.model.Basket;
import schwarz.jobs.interview.coupon.util.Constants;
import schwarz.jobs.interview.coupon.web.dto.ApplicationRequestDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;

/**
 * REST Controller for creating and updating the basket.
 * 
 * This controller provides endpoints to create coupon, update basket with code.
 * It also provides an endpoint to get all the coupons
 * 
 * @author manik sharma
 */

@Controller
@RequiredArgsConstructor
@RequestMapping(Constants.PATH_SEPARATOR + Constants.BASE_URL)
@Slf4j
public class CouponResource {

	
    private final CouponService couponService;

    /**
     * @param request containing {@link ApplicationRequestDTO} that provides code and basket to be updated
     * @return a {@link ResponseEntity} containing the updated {@link Basket}
     */
    @ApiOperation(value = "Applies currently active promotions and coupons from the request to the requested Basket")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully updated the basket", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Basket.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json"))})
    @PostMapping(value = Constants.PATH_SEPARATOR + Constants.ENDPOINT_APPLY)
    public ResponseEntity<Basket> apply(
        @RequestBody @Valid final ApplicationRequestDTO applicationRequestDTO) {

        log.info("Applying coupon started!");

        final Optional<Basket> basket =
            couponService.apply(applicationRequestDTO.getBasket(), applicationRequestDTO.getCode());

        if (basket.isEmpty()) {
        	log.info(Constants.COUPON_APPLIED_FAIL);
            return ResponseEntity.notFound().build();
        }

        if (!applicationRequestDTO.getBasket().isApplicationSuccessful()) {
        	log.info(Constants.COUPON_APPLIED_FAIL);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        log.info("Applied coupon End!");

        return ResponseEntity.ok().body(applicationRequestDTO.getBasket());
    }
    
    
    /**
     * @param request containing {@link CouponDTO} that creates a new coupon in server
     * @return a {@link ResponseEntity} containing the updated {@link Basket}
     */
    @ApiOperation(value = "Creates a new Coupon in the server")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully created the coupon", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Basket.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json"))})
    @PostMapping(value = Constants.PATH_SEPARATOR + Constants.ENDPOINT_CREATE)
    public ResponseEntity<CouponDTO> create(@RequestBody @Valid final CouponDTO couponDTO) {
    	log.info("Inside creating coupon!");
        final Coupon coupon = couponService.createCoupon(couponDTO);
        if(coupon!=null) {
        	log.info(Constants.COUPON_CREATE_SUCCESS);
        	return ResponseEntity.ok(couponDTO);
        }else {
        	log.info(Constants.COUPON_CREATE_FAIL);
        	return ResponseEntity.notFound().build();
        }
        
    }

//    @GetMapping("/coupons")
//    public List<Coupon> getCoupons(@RequestBody @Valid final CouponRequestDTO couponRequestDTO) {
//
//        return couponService.getCoupons(couponRequestDTO);
//    }
    /**
     * 
     * @return a {@link ResponseEntity} containing the list of coupons {@link CouponDTO}
     */
    @ApiOperation(value = "Gets the Coupons from the server")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully fetched all the coupons", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponDTO.class))),
      @ApiResponse(responseCode = "404", description = "No data found", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json"))})
    @GetMapping(value = Constants.PATH_SEPARATOR + Constants.ENDPOINT_COUPONS)
    public ResponseEntity<List<CouponDTO>> getCoupons(@RequestParam List<String> couponCodes) {
    	log.info("Get all coupons!");
    	List<CouponDTO> lst = couponService.getCoupons(couponCodes);
    	if(!lst.isEmpty()) {
    		return ResponseEntity.ok(lst);
    	}else {
    		return ResponseEntity.noContent().build();
    	}
        
    }
}
