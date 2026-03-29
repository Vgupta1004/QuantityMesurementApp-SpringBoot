package com.seveneleven.quantitymeasurement.unit;

public enum LengthUnit implements IMeasurable{
	
	/**
	 * All supported length units.
	 * Base unit = INCHES. Every conversion factor is relative to 1 inch.
	 *
	 * To convert between two units:
	 *   result = (value * fromUnit.factor) / toUnit.factor
	 */
	
	// 1 inch = 1 inch (base)
    INCHES(1.0),

    // 1 foot = 12 inches
    FEET(12.0),

    // 1 yard = 36 inches
    YARDS(36.0),

    // 1 centimeter = 0.3937 inches
    CENTIMETERS(0.3937),

    // 1 meter = 39.3701 inches
    METERS(39.3701);
	
	private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }
    
    @Override
    public String getUnitName() {
        return this.name();
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }
	
}
