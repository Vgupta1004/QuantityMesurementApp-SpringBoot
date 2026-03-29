package com.seveneleven.quantitymeasurement.unit;


/**
 * Temperature units.
 *
 * Temperature cannot use a simple multiplication factor because the scales
 * have different zero points. So instead of getConversionFactor(), the
 * service layer handles temperature conversion with explicit formulas:
 *
 *   Celsius to Fahrenheit : (C * 9/5) + 32
 *   Fahrenheit to Celsius : (F - 32) * 5/9
 *
 * getConversionFactor() returns 1.0 as a placeholder — the service
 * detects TemperatureUnit and uses the formulas above instead.
 */
public enum TemperatureUnit implements IMeasurable{
	
	CELSIUS(1.0),
    FAHRENHEIT(1.0);

    private final double conversionFactor;

    TemperatureUnit(double conversionFactor) {
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
