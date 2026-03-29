package com.seveneleven.quantitymeasurement.service;

import java.util.List;
import java.util.function.DoubleBinaryOperator;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seveneleven.quantitymeasurement.exception.QuantityMeasurementException;
import com.seveneleven.quantitymeasurement.model.OperationType;
import com.seveneleven.quantitymeasurement.model.QuantityDTO;
import com.seveneleven.quantitymeasurement.model.QuantityMeasurementDTO;
import com.seveneleven.quantitymeasurement.model.QuantityMeasurementEntity;
import com.seveneleven.quantitymeasurement.model.QuantityModel;
import com.seveneleven.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.seveneleven.quantitymeasurement.unit.IMeasurable;
import com.seveneleven.quantitymeasurement.unit.LengthUnit;
import com.seveneleven.quantitymeasurement.unit.TemperatureUnit;
import com.seveneleven.quantitymeasurement.unit.VolumeUnit;
import com.seveneleven.quantitymeasurement.unit.WeightUnit;

/**
* Implementation of all quantity measurement business logic.
*/
@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService{

	private static final Logger logger = Logger.getLogger(QuantityMeasurementServiceImpl.class.getName());
	
	@Autowired
    private QuantityMeasurementRepository repository;
	
	@Override
    public QuantityMeasurementDTO compare(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO) {

        QuantityMeasurementDTO result = new QuantityMeasurementDTO();
        result.setThisValue(thisQuantityDTO.getValue());
        result.setThisUnit(thisQuantityDTO.getUnit());
        result.setThisMeasurementType(thisQuantityDTO.getMeasurementType());
        result.setThatValue(thatQuantityDTO.getValue());
        result.setThatUnit(thatQuantityDTO.getUnit());
        result.setThatMeasurementType(thatQuantityDTO.getMeasurementType());
        result.setOperation(OperationType.COMPARE.name());

        try {
            QuantityModel<IMeasurable> thisModel = convertDtoToModel(thisQuantityDTO);
            QuantityModel<IMeasurable> thatModel = convertDtoToModel(thatQuantityDTO);

            // Compare by converting both to base units, then checking equality
            boolean isEqual = compare(thisModel, thatModel);

            result.setResultString(String.valueOf(isEqual));
            result.setResultValue(0.0);
            result.setError(false);

            logger.info("COMPARE: " + thisQuantityDTO.getValue() +
                    " " + thisQuantityDTO.getUnit() + " vs " +
                    thatQuantityDTO.getValue() + " " + thatQuantityDTO.getUnit() +
                    " = " + isEqual);

        } catch (Exception e) {
            logger.warning("COMPARE error: " + e.getMessage());
            result.setError(true);
            result.setErrorMessage("compare Error: " + e.getMessage());
        }

        // Save to DB regardless of success or failure
        repository.save(result.toEntity());
        return result;
    }
	
	@Override
    public QuantityMeasurementDTO convert(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO) {

        QuantityMeasurementDTO result = new QuantityMeasurementDTO();
        result.setThisValue(thisQuantityDTO.getValue());
        result.setThisUnit(thisQuantityDTO.getUnit());
        result.setThisMeasurementType(thisQuantityDTO.getMeasurementType());
        result.setThatValue(thatQuantityDTO.getValue());
        result.setThatUnit(thatQuantityDTO.getUnit());
        result.setThatMeasurementType(thatQuantityDTO.getMeasurementType());
        result.setOperation(OperationType.CONVERT.name());

        try {
            QuantityModel<IMeasurable> thisModel = convertDtoToModel(thisQuantityDTO);
            QuantityModel<IMeasurable> targetModel = convertDtoToModel(thatQuantityDTO);

            double converted = convertTo(thisModel, targetModel.getUnit());

            result.setResultValue(converted);
            result.setResultUnit(thatQuantityDTO.getUnit());
            result.setResultMeasurementType(thatQuantityDTO.getMeasurementType());
            result.setError(false);

            logger.info("CONVERT: " + thisQuantityDTO.getValue() +
                    " " + thisQuantityDTO.getUnit() +
                    " -> " + converted + " " + thatQuantityDTO.getUnit());

        } catch (Exception e) {
            logger.warning("CONVERT error: " + e.getMessage());
            result.setError(true);
            result.setErrorMessage("convert Error: " + e.getMessage());
        }

        repository.save(result.toEntity());
        return result;
    }
	
	public QuantityMeasurementDTO add(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO) {
        // Delegate to the version with target unit, using thisQuantityDTO as target
        return add(thisQuantityDTO, thatQuantityDTO, thisQuantityDTO);
    }
	
	@Override
    public QuantityMeasurementDTO add(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO,
            QuantityDTO targetUnitDTO) {

        QuantityMeasurementDTO result = new QuantityMeasurementDTO();
        result.setThisValue(thisQuantityDTO.getValue());
        result.setThisUnit(thisQuantityDTO.getUnit());
        result.setThisMeasurementType(thisQuantityDTO.getMeasurementType());
        result.setThatValue(thatQuantityDTO.getValue());
        result.setThatUnit(thatQuantityDTO.getUnit());
        result.setThatMeasurementType(thatQuantityDTO.getMeasurementType());
        result.setOperation(OperationType.ADD.name());

        try {
            QuantityModel<IMeasurable> thisModel = convertDtoToModel(thisQuantityDTO);
            QuantityModel<IMeasurable> thatModel = convertDtoToModel(thatQuantityDTO);
            QuantityModel<IMeasurable> targetModel = convertDtoToModel(targetUnitDTO);

            double addResult = performArithmetic(
                    thisModel, thatModel, targetModel.getUnit(),
                    (a, b) -> a + b
            );

            result.setResultValue(addResult);
            result.setResultUnit(targetUnitDTO.getUnit());
            result.setResultMeasurementType(targetUnitDTO.getMeasurementType());
            result.setError(false);

        } catch (Exception e) {
            logger.warning("ADD error: " + e.getMessage());
            result.setError(true);
            result.setErrorMessage("add Error: " + e.getMessage());
        }

        repository.save(result.toEntity());
        return result;
    }
	
	@Override
    public QuantityMeasurementDTO subtract(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO) {
        return subtract(thisQuantityDTO, thatQuantityDTO, thisQuantityDTO);
    }
	
	@Override
    public QuantityMeasurementDTO subtract(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO,
            QuantityDTO targetUnitDTO) {

        QuantityMeasurementDTO result = new QuantityMeasurementDTO();
        result.setThisValue(thisQuantityDTO.getValue());
        result.setThisUnit(thisQuantityDTO.getUnit());
        result.setThisMeasurementType(thisQuantityDTO.getMeasurementType());
        result.setThatValue(thatQuantityDTO.getValue());
        result.setThatUnit(thatQuantityDTO.getUnit());
        result.setThatMeasurementType(thatQuantityDTO.getMeasurementType());
        result.setOperation(OperationType.SUBTRACT.name());

        try {
            QuantityModel<IMeasurable> thisModel = convertDtoToModel(thisQuantityDTO);
            QuantityModel<IMeasurable> thatModel = convertDtoToModel(thatQuantityDTO);
            QuantityModel<IMeasurable> targetModel = convertDtoToModel(targetUnitDTO);

            double subtractResult = performArithmetic(
                    thisModel, thatModel, targetModel.getUnit(),
                    (a, b) -> a - b
            );

            result.setResultValue(subtractResult);
            result.setResultUnit(targetUnitDTO.getUnit());
            result.setResultMeasurementType(targetUnitDTO.getMeasurementType());
            result.setError(false);

        } catch (Exception e) {
            logger.warning("SUBTRACT error: " + e.getMessage());
            result.setError(true);
            result.setErrorMessage("subtract Error: " + e.getMessage());
        }

        repository.save(result.toEntity());
        return result;
    }
	
	@Override
    public QuantityMeasurementDTO multiply(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO) {

        QuantityMeasurementDTO result = new QuantityMeasurementDTO();
        result.setThisValue(thisQuantityDTO.getValue());
        result.setThisUnit(thisQuantityDTO.getUnit());
        result.setThisMeasurementType(thisQuantityDTO.getMeasurementType());
        result.setThatValue(thatQuantityDTO.getValue());
        result.setThatUnit(thatQuantityDTO.getUnit());
        result.setThatMeasurementType(thatQuantityDTO.getMeasurementType());
        result.setOperation(OperationType.MULTIPLY.name());

        try {
            QuantityModel<IMeasurable> thisModel = convertDtoToModel(thisQuantityDTO);
            QuantityModel<IMeasurable> thatModel = convertDtoToModel(thatQuantityDTO);

            double multiplyResult = performArithmetic(
                    thisModel, thatModel, thisModel.getUnit(),
                    (a, b) -> a * b
            );

            result.setResultValue(multiplyResult);
            result.setResultUnit(thisQuantityDTO.getUnit());
            result.setResultMeasurementType(thisQuantityDTO.getMeasurementType());
            result.setError(false);

        } catch (Exception e) {
            logger.warning("MULTIPLY error: " + e.getMessage());
            result.setError(true);
            result.setErrorMessage("multiply Error: " + e.getMessage());
        }

        repository.save(result.toEntity());
        return result;
    }
	
	@Override
    public QuantityMeasurementDTO divide(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO) {

        QuantityMeasurementDTO result = new QuantityMeasurementDTO();
        result.setThisValue(thisQuantityDTO.getValue());
        result.setThisUnit(thisQuantityDTO.getUnit());
        result.setThisMeasurementType(thisQuantityDTO.getMeasurementType());
        result.setThatValue(thatQuantityDTO.getValue());
        result.setThatUnit(thatQuantityDTO.getUnit());
        result.setThatMeasurementType(thatQuantityDTO.getMeasurementType());
        result.setOperation(OperationType.DIVIDE.name());

        try {
            QuantityModel<IMeasurable> thisModel = convertDtoToModel(thisQuantityDTO);
            QuantityModel<IMeasurable> thatModel = convertDtoToModel(thatQuantityDTO);

            // Convert thatQuantity to base units to check for zero
            double thatBase = thatModel.toBaseUnit();
            if (thatBase == 0.0) {
                throw new ArithmeticException("Divide by zero");
            }

            double divideResult = performArithmetic(
                    thisModel, thatModel, thisModel.getUnit(),
                    (a, b) -> a / b
            );

            result.setResultValue(divideResult);
            result.setResultUnit(thisQuantityDTO.getUnit());
            result.setResultMeasurementType(thisQuantityDTO.getMeasurementType());
            result.setError(false);

        } catch (Exception e) {
            logger.warning("DIVIDE error: " + e.getMessage());
            result.setError(true);
            result.setErrorMessage(e.getMessage());
        }

        repository.save(result.toEntity());
        return result;
    }
	
	@Override
    public List<QuantityMeasurementDTO> getOperationHistory(String operation) {
        List<QuantityMeasurementEntity> entities =
                repository.findByOperation(operation.toUpperCase());
        return QuantityMeasurementDTO.fromEntityList(entities);
    }

    @Override
    public List<QuantityMeasurementDTO> getMeasurementsByType(String type) {
        List<QuantityMeasurementEntity> entities =
                repository.findByThisMeasurementType(type);
        return QuantityMeasurementDTO.fromEntityList(entities);
    }
    
    @Override
    public long getOperationCount(String operation) {
        return repository.countByOperationAndIsErrorFalse(operation.toUpperCase());
    }

    @Override
    public List<QuantityMeasurementDTO> getErrorHistory() {
        List<QuantityMeasurementEntity> entities =
                repository.findByIsErrorTrue();
        return QuantityMeasurementDTO.fromEntityList(entities);
    }
    
    private QuantityModel<IMeasurable> convertDtoToModel(QuantityDTO dto) {
        IMeasurable unit;
        switch (dto.getMeasurementType()) {
            case "LengthUnit":
                unit = LengthUnit.valueOf(dto.getUnit());
                break;
            case "VolumeUnit":
                unit = VolumeUnit.valueOf(dto.getUnit());
                break;
            case "WeightUnit":
                unit = WeightUnit.valueOf(dto.getUnit());
                break;
            case "TemperatureUnit":
                unit = TemperatureUnit.valueOf(dto.getUnit());
                break;
            default:
                throw new QuantityMeasurementException(
                        "Unknown measurement type: " + dto.getMeasurementType());
        }
        return new QuantityModel<>(dto.getValue(), unit);
    }
    
    private <U extends IMeasurable> boolean compare(
            QuantityModel<U> thisModel,
            QuantityModel<U> thatModel) {

        validateSameMeasurementCategory(thisModel, thatModel);
        double epsilon = 1e-9;
        return Math.abs(thisModel.toBaseUnit() - thatModel.toBaseUnit()) < epsilon;
    }
    
    private <U extends IMeasurable> double convertTo(
            QuantityModel<U> model,
            IMeasurable targetUnit) {

        // Handle temperature separately because it has zero-point offsets
        if (model.getUnit() instanceof TemperatureUnit) {
            return convertTemperatureUnit(model.getValue(),
                    (TemperatureUnit) model.getUnit(),
                    (TemperatureUnit) targetUnit);
        }

        // Standard unit conversion: to base, then to target
        double baseValue = model.toBaseUnit();
        return baseValue / targetUnit.getConversionFactor();
    }
    
    private double convertTemperatureUnit(
            double value,
            TemperatureUnit from,
            TemperatureUnit to) {

        if (from == to) return value;
        if (from == TemperatureUnit.CELSIUS && to == TemperatureUnit.FAHRENHEIT) {
            return (value * 9.0 / 5.0) + 32.0;
        }
        if (from == TemperatureUnit.FAHRENHEIT && to == TemperatureUnit.CELSIUS) {
            return (value - 32.0) * 5.0 / 9.0;
        }
        throw new QuantityMeasurementException(
                "Unsupported temperature conversion: " + from + " to " + to);
    }
    
    private <U extends IMeasurable> double performArithmetic(
            QuantityModel<U> thisModel,
            QuantityModel<U> thatModel,
            IMeasurable targetUnit,
            DoubleBinaryOperator operation) {

        validateSameMeasurementCategory(thisModel, thatModel);

        double thisBase = thisModel.toBaseUnit();
        double thatBase = thatModel.toBaseUnit();

        double resultBase = operation.applyAsDouble(thisBase, thatBase);

        // Convert result back from base units to the requested target unit
        return resultBase / targetUnit.getConversionFactor();
    }
    
    private <U extends IMeasurable> void validateSameMeasurementCategory(
            QuantityModel<U> thisModel,
            QuantityModel<U> thatModel) {

        String thisType = thisModel.getUnit().getClass().getSimpleName();
        String thatType = thatModel.getUnit().getClass().getSimpleName();

        if (!thisType.equals(thatType)) {
            throw new QuantityMeasurementException(
                    "Cannot perform arithmetic between different measurement " +
                    "categories: " + thisType + " and " + thatType);
        }
    }
	
}
