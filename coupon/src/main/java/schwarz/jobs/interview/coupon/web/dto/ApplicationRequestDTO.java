package schwarz.jobs.interview.coupon.web.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import schwarz.jobs.interview.coupon.core.services.model.Basket;

@Data
@Builder
@AllArgsConstructor
public class ApplicationRequestDTO {

    @NotBlank
    private String code;

    @NotNull
    private Basket basket;

}
