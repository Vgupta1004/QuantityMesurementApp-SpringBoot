package com.seveneleven.quantitymeasurement.model;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantityMeasurementDTO {
	
	private double thisValue;
    private String thisUnit;
    private String thisMeasurementType;

    private double thatValue;
    private String thatUnit;
    private String thatMeasurementType;
    
    private String operation;
    
    private String resultString;     // for COMPARE: "true" / "false"
    private double resultValue;      // for arithmetic / convert
    private String resultUnit;
    private String resultMeasurementType;
    
    private String errorMessage;
    
    @JsonProperty("error")
    private boolean error;
    
    /**
     * Converts one JPA entity (from the database) into a DTO
     * (for the API response). Called after loading data from the repo.
     */
    public static QuantityMeasurementDTO fromEntity(QuantityMeasurementEntity entity) {
        if (entity == null) return null;

        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setThisValue(entity.getThisValue());
        dto.setThisUnit(entity.getThisUnit());
        dto.setThisMeasurementType(entity.getThisMeasurementType());
        dto.setThatValue(entity.getThatValue());
        dto.setThatUnit(entity.getThatUnit());
        dto.setThatMeasurementType(entity.getThatMeasurementType());
        dto.setOperation(entity.getOperation());
        dto.setResultString(entity.getResultString());
        dto.setResultValue(entity.getResultValue());
        dto.setResultUnit(entity.getResultUnit());
        dto.setResultMeasurementType(entity.getResultMeasurementType());
        dto.setErrorMessage(entity.getErrorMessage());
        dto.setError(entity.isError());
        return dto;
    }
    
    /**
     * Converts this DTO into a JPA entity for saving to the database.
     * Called in the service layer after every operation.
     */
    public QuantityMeasurementEntity toEntity() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(this.thisValue);
        entity.setThisUnit(this.thisUnit);
        entity.setThisMeasurementType(this.thisMeasurementType);
        entity.setThatValue(this.thatValue);
        entity.setThatUnit(this.thatUnit);
        entity.setThatMeasurementType(this.thatMeasurementType);
        entity.setOperation(this.operation);
        entity.setResultString(this.resultString);
        entity.setResultValue(this.resultValue);
        entity.setResultUnit(this.resultUnit);
        entity.setResultMeasurementType(this.resultMeasurementType);
        entity.setErrorMessage(this.errorMessage);
        entity.setError(this.error);
        return entity;
    }
    
    /**
     * Converts a list of entities to a list of DTOs.
     * Used when returning history from GET endpoints.
     *
     */
    public static List<QuantityMeasurementDTO> fromEntityList(
            List<QuantityMeasurementEntity> entities) {
        return entities.stream()
                .map(QuantityMeasurementDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Converts a list of DTOs to a list of entities.
     * Less commonly used but provided for symmetry.
     */
    public static List<QuantityMeasurementEntity> toEntityList(
            List<QuantityMeasurementDTO> dtos) {
        return dtos.stream()
                .map(QuantityMeasurementDTO::toEntity)
                .collect(Collectors.toList());
    }
	
}
