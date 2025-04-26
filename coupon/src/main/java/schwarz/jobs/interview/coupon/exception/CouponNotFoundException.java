package schwarz.jobs.interview.coupon.exception;

/**
 * CouponNotFoundException for returning custom Exception.
 * <p>
 * This class is a custom Exception to handle invalid coupon.
 * </p>
 */

public class CouponNotFoundException extends RuntimeException{

	public CouponNotFoundException(String message) {
		super(message);
	}
}
