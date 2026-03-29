package com.seveneleven.quantitymeasurement.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityInputDTO {
	
	@Valid
    @NotNull(message = "First quantity cannot be null")
    private QuantityDTO thisQuantityDTO;

    @Valid
    @NotNull(message = "Second quantity cannot be null")
    private QuantityDTO thatQuantityDTO;
    
    @Valid
    private QuantityDTO targetQuantityDTO;
	
}
