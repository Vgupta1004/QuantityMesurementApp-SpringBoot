package com.seveneleven.quantitymeasurement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seveneleven.quantitymeasurement.model.QuantityMeasurementEntity;

public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long>{
	
	/**
     * Find all measurements for a given operation type.
     * Spring generates: SELECT * FROM quantity_measurement_entity
     *                   WHERE operation = ?
     *
     */
    List<QuantityMeasurementEntity> findByOperation(String operation);
    
    
    /**
     * Find all measurements for a given measurement type.
     * Spring generates: SELECT * FROM quantity_measurement_entity
     *                   WHERE this_measurement_type = ?
     *
     */
    List<QuantityMeasurementEntity> findByThisMeasurementType(String measurementType);
    
    
    /**
     * Find all measurements created after a given date/time.
     * Spring generates: SELECT * FROM ... WHERE created_at > ?
     *
     * Useful for dashboards showing "activity in the last 24 hours".
     */
    List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime date);
    
    
    /**
     * Custom JPQL query — finds successful (non-error) records for an operation.
     *
     * JPQL uses the Java CLASS name (QuantityMeasurementEntity) and
     * FIELD names (e.isError), NOT the table/column names.
     * Hibernate translates this to real SQL automatically.
     *
     * @Param("operation") maps the method parameter to :operation in the query.
     */
    @Query("SELECT e FROM QuantityMeasurementEntity e " +
           "WHERE e.operation = :operation AND e.isError = false")
    List<QuantityMeasurementEntity> findSuccessfulOperations(@Param("operation") String operation);
    
    
    /**
     * Count successful records for a given operation type.
     * Spring generates: SELECT COUNT(*) FROM ...
     *                   WHERE operation = ? AND is_error = false
     *
     * Useful for: "how many successful COMPARE operations have been run?"
     */
    long countByOperationAndIsErrorFalse(String operation);
    
    
    /**
     * Find all records that resulted in an error.
     * Spring generates: SELECT * FROM ... WHERE is_error = true
     *
     * Useful for the /history/errored endpoint.
     */
    List<QuantityMeasurementEntity> findByIsErrorTrue();
	
}
