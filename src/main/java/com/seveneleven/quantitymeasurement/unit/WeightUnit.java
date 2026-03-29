package com.seveneleven.quantitymeasurement.unit;


/**
 * All supported weight units.
 * Base unit = MILLIGRAMS. Every conversion factor is relative to 1 mg.
 */
public enum WeightUnit implements IMeasurable{
	
	// 1 milligram = 1 mg (base)
    MILLIGRAM(1.0),

    // 1 gram = 1000 mg
    GRAM(1000.0),

    // 1 kilogram = 1,000,000 mg
    KILOGRAM(1000000.0),

    // 1 pound = 453,592 mg
    POUND(453592.0),

    // 1 tonne = 1,000,000,000 mg
    TONNE(1000000000.0);
	
	private final double conversionFactor;

    WeightUnit(double conversionFactor) {
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
