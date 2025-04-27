package schwarz.jobs.interview.coupon.exception;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents an error response returned in case of API errors.
 * <p>
 * This record is used to provide structured information when an error occurs in the API.
 * It includes the timestamp of the error, the HTTP status, the error message,
 * a list of additional messages (if any), and the path of the request that caused the error.
 * </p>
 *
 * @param timestamp The timestamp when the error occurred.
 * @param status    The HTTP status code of the error.
 * @param error     A brief description of the error.
 * @param messages  A list of detailed error messages, such as validation failures or other issues.
 * @param path      The path of the request that triggered the error.
 */
@Data
@AllArgsConstructor
public class ApiErrorResponse{
	int status; 
	String error; 
	List<String> messages;
	String path;
}
                               