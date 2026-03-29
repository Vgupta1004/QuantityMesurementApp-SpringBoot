package com.seveneleven.quantitymeasurement.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

/**
 * JPA Entity — maps this Java class to a database table called
 * "quantity_measurement_entity".
 *
 * Hibernate reads the annotations below and automatically:
 *   1. Creates the table when the app starts (ddl-auto=update)
 *   2. Handles INSERT, SELECT, UPDATE, DELETE for us
 *   3. Maps each field to the correct column
 *
 * Lombok annotations:
 *   @Data             — generates getters, setters, toString, equals, hashCode
 *   @NoArgsConstructor — generates a no-arg constructor (required by JPA spec)
 *   @AllArgsConstructor — generates a constructor with every field as a parameter
 */

@Entity
@Table(
	name = "quantity_measurement_entity",
	indexes = {
			@Index(name = "idx_operation",        columnList = "operation"),
	        @Index(name = "idx_measurement_type", columnList = "this_measurement_type"),
	        @Index(name = "idx_created_at",       columnList = "created_at")
	}
)
@Data
//@NoArgsConstructor
@AllArgsConstructor
public class QuantityMeasurementEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "this_value", nullable = false)
    private double thisValue;

    @Column(name = "this_unit", nullable = false)
    private String thisUnit;

    @Column(name = "this_measurement_type", nullable = false)
    private String thisMeasurementType;
    
    @Column(name = "that_value", nullable = false)
    private double thatValue;

    @Column(name = "that_unit", nullable = false)
    private String thatUnit;

    @Column(name = "that_measurement_type", nullable = false)
    private String thatMeasurementType;
    
    @Column(name = "operation", nullable = false)
    private String operation;
    
    @Column(name = "result_string")
    private String resultString;
    
    @Column(name = "is_error")
    private boolean isError;

    @Column(name = "error_message")
    private String errorMessage;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
	
}
