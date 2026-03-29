package com.seveneleven.quantitymeasurement.service;

import java.util.List;

import com.seveneleven.quantitymeasurement.model.QuantityDTO;
import com.seveneleven.quantitymeasurement.model.QuantityMeasurementDTO;

/**
 * Contract for all quantity measurement operations.
 *
 * The REST controller depends on THIS interface, not the implementation.
 *
 * All methods return QuantityMeasurementDTO — a structured response
 * that includes the inputs, operation type, result, and any error info.
 */
public interface IQuantityMeasurementService {
	
	/**
     * Compare two quantities for equality after unit conversion.
     * e.g. 1 FOOT vs 12 INCHES -> resultString = "true"
     */
    QuantityMeasurementDTO compare(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO
    );

    /**
     * Convert a quantity from one unit to another within the same type.
     * e.g. 1 FOOT -> INCHES -> resultValue = 12.0
     * thatQuantityDTO provides the target unit (its value is ignored).
     */
    QuantityMeasurementDTO convert(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO
    );
    
    /**
     * Add two quantities. Result is in the same unit as thisQuantityDTO.
     * e.g. 1 FOOT + 12 INCHES -> resultValue = 2.0 FEET
     */
    QuantityMeasurementDTO add(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO
    );

    /**
     * Add two quantities and express result in a specific target unit.
     * e.g. 1 FOOT + 12 INCHES -> result in INCHES -> resultValue = 24.0
     */
    QuantityMeasurementDTO add(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO,
            QuantityDTO targetUnitDTO
    );
    
    /**
     * Subtract thatQuantity from thisQuantity.
     * Result is in the same unit as thisQuantityDTO.
     * e.g. 2 FEET - 12 INCHES -> resultValue = 1.0 FEET
     */
    QuantityMeasurementDTO subtract(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO
    );

    /**
     * Subtract with a specific target unit for the result.
     */
    QuantityMeasurementDTO subtract(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO,
            QuantityDTO targetUnitDTO
    );
    
    /**
     * Multiply thisQuantity by thatQuantity.
     * Result is in the same unit as thisQuantityDTO.
     */
    QuantityMeasurementDTO multiply(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO
    );

    /**
     * Divide thisQuantity by thatQuantity.
     * Throws QuantityMeasurementException if thatQuantity is zero.
     */
    QuantityMeasurementDTO divide(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO
    );
    
    /**
     * Get full history of a specific operation type.
     * e.g. getOperationHistory("COMPARE") returns all compare records.
     */
    List<QuantityMeasurementDTO> getOperationHistory(String operation);

    /**
     * Get history filtered by measurement type.
     * e.g. getMeasurementsByType("LengthUnit") returns all length operations.
     */
    List<QuantityMeasurementDTO> getMeasurementsByType(String type);
    
    /**
     * Count how many times a specific operation has been performed.
     * e.g. getOperationCount("ADD") -> 5
     */
    long getOperationCount(String operation);

    /**
     * Get all operations that resulted in an error.
     * Useful for debugging and audit trails.
     */
    List<QuantityMeasurementDTO> getErrorHistory();
	
}
