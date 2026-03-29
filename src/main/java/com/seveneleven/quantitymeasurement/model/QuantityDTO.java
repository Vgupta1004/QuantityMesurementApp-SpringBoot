package com.seveneleven.quantitymeasurement.model;

import com.seveneleven.quantitymeasurement.unit.LengthUnit;
import com.seveneleven.quantitymeasurement.unit.TemperatureUnit;
import com.seveneleven.quantitymeasurement.unit.VolumeUnit;
import com.seveneleven.quantitymeasurement.unit.WeightUnit;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityDTO {
	
	/**
     * The numeric value of the quantity.
     * @NotNull ensures the field is present in the JSON body.
     */
    @NotNull(message = "Value cannot be empty")
    private Double value;
    
    
    /**
     * The unit name, e.g. "FEET", "KILOGRAM", "CELSIUS".
     * @NotEmpty ensures it is not null and not an empty string.
     * The @AssertTrue method below validates it actually matches
     * the declared measurementType.
     */
    @NotNull(message = "Unit cannot be null")
    private String unit;
    
    
    /**
     * The measurement category.
     * @Pattern ensures only the four supported types are accepted.
     * Any other value (e.g. "SpeedUnit") returns 400 immediately.
     */
    @NotNull(message = "Measurement type cannot be null")
    @Pattern(
            regexp = "LengthUnit|VolumeUnit|WeightUnit|TemperatureUnit",
            message = "Measurement type must be one of: " +
                      "LengthUnit, VolumeUnit, WeightUnit, TemperatureUnit"
    )
    private String measurementType;
    
    
    @AssertTrue(message = "Unit must be valid for the specified measurement type")
    public boolean isValidUnit() {
        if (unit == null || measurementType == null) {
            return true; // other @NotNull annotations handle this
        }
        try {
            switch (measurementType) {
                case "LengthUnit":      LengthUnit.valueOf(unit);      break;
                case "VolumeUnit":      VolumeUnit.valueOf(unit);      break;
                case "WeightUnit":      WeightUnit.valueOf(unit);      break;
                case "TemperatureUnit": TemperatureUnit.valueOf(unit); break;
                default: return false;
            }
            return true;
        } catch (IllegalArgumentException e) {
            // valueOf() throws this if the string doesn't match any enum constant
            return false;
        }
    }
	
}
