package com.seveneleven.quantitymeasurement.model;

public enum OperationType {
	
	/** Check if two quantities are equal after unit conversion */
    COMPARE,

    /** Convert a quantity from one unit to another */
    CONVERT,

    /** Add two quantities together */
    ADD,

    /** Subtract one quantity from another */
    SUBTRACT,

    /** Multiply two quantities */
    MULTIPLY,

    /** Divide one quantity by another */
    DIVIDE;
	
	public String getDisplayName() {
        return this.name().toLowerCase();
    }
	
}
