package com.seveneleven.quantitymeasurement.unit;

/**
 * All supported volume units.
 * Base unit = MILLILITERS. Every conversion factor is relative to 1 ml.
 */
public enum VolumeUnit implements IMeasurable {
	
	// 1 milliliter = 1 ml (base)
    MILLILITER(1.0),

    // 1 liter = 1000 ml
    LITRE(1000.0),

    // 1 gallon = 3785.41 ml
    GALLON(3785.41),

    // 1 cup = 236.588 ml
    CUP(236.588);
	
	private final double conversionFactor;

    VolumeUnit(double conversionFactor) {
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
