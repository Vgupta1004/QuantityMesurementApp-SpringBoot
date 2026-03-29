package com.seveneleven.quantitymeasurement.unit;


/**
 * Contract that every measurable unit must fulfil.
 *
 * Every unit enum (LengthUnit, WeightUnit, VolumeUnit, TemperatureUnit)
 * implements this interface. This lets the service layer work with any
 * unit type generically — e.g. QuantityModel<IMeasurable> — without
 * knowing which specific unit it is dealing with at compile time.
 */
public interface IMeasurable {
	
	/**
     * Returns the display name of the unit, e.g. "FEET", "KILOGRAM".
     * Each enum provides this via its built-in name() method.
     */
    String getUnitName();

    /**
     * Returns the conversion factor to the base unit for this measurement
     * type. For example:
     *   LengthUnit base = INCHES
     *   FEET.getConversionFactor() = 12.0  (1 foot = 12 inches)
     *   CENTIMETERS.getConversionFactor() = 0.3937 (1 cm = 0.3937 inches)
     */
    double getConversionFactor();
    
}
