package com.seveneleven.quantitymeasurement.model;

import com.seveneleven.quantitymeasurement.unit.IMeasurable;

/** 
 * This is the object the service layer actually works with when performing
 * comparisons, conversions, and arithmetic. The DTOs convert to/from this.
 */
public class QuantityModel<U extends IMeasurable> {
	
	private final double value;
    private final U unit;

    public QuantityModel(double value, U unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }
    
    public double toBaseUnit() {
        return value * unit.getConversionFactor();
    }

    @Override
    public String toString() {
        return value + " " + unit.getUnitName();
    }
	
}
