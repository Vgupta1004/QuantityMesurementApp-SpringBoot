package com.seveneleven.quantitymeasurement.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seveneleven.quantitymeasurement.model.QuantityInputDTO;
import com.seveneleven.quantitymeasurement.model.QuantityMeasurementDTO;
import com.seveneleven.quantitymeasurement.service.IQuantityMeasurementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/quantities")
@Tag(
    name = "Quantity Measurements",
    description = "REST API for quantity measurement operations"
)
public class QuantityMeasurementController {
	
	private static final Logger logger = Logger.getLogger(QuantityMeasurementController.class.getName());
	
	private static final String EX_FEET_INCH =
            "{ \"thisQuantityDTO\": {\"value\": 1.0, \"unit\": \"FEET\", " +
            "\"measurementType\": \"LengthUnit\"}, " +
            "\"thatQuantityDTO\": {\"value\": 12.0, \"unit\": \"INCHES\", " +
            "\"measurementType\": \"LengthUnit\"} }";

    private static final String EX_YARD_FEET =
            "{ \"thisQuantityDTO\": {\"value\": 1.0, \"unit\": \"YARDS\", " +
            "\"measurementType\": \"LengthUnit\"}, " +
            "\"thatQuantityDTO\": {\"value\": 3.0, \"unit\": \"FEET\", " +
            "\"measurementType\": \"LengthUnit\"} }";

    private static final String EX_GALLON_LITRE =
            "{ \"thisQuantityDTO\": {\"value\": 1.0, \"unit\": \"GALLON\", " +
            "\"measurementType\": \"VolumeUnit\"}, " +
            "\"thatQuantityDTO\": {\"value\": 3.785, \"unit\": \"LITRE\", " +
            "\"measurementType\": \"VolumeUnit\"} }";

    private static final String EX_TEMP =
            "{ \"thisQuantityDTO\": {\"value\": 212.0, \"unit\": \"FAHRENHEIT\", " +
            "\"measurementType\": \"TemperatureUnit\"}, " +
            "\"thatQuantityDTO\": {\"value\": 100.0, \"unit\": \"CELSIUS\", " +
            "\"measurementType\": \"TemperatureUnit\"} }";

    private static final String EX_WITH_TARGET =
            "{ \"thisQuantityDTO\": {\"value\": 1.0, \"unit\": \"FEET\", " +
            "\"measurementType\": \"LengthUnit\"}, " +
            "\"thatQuantityDTO\": {\"value\": 12.0, \"unit\": \"INCHES\", " +
            "\"measurementType\": \"LengthUnit\"}, " +
            "\"targetQuantityDTO\": {\"value\": 0.0, \"unit\": \"INCHES\", " +
            "\"measurementType\": \"LengthUnit\"} }";
    
    @Autowired
    private IQuantityMeasurementService service;
    
    @PostMapping("/compare")
    @Operation(summary = "Compare two quantities",
               description = "Returns true if the two quantities are equal " +
                             "after unit conversion")
    public ResponseEntity<QuantityMeasurementDTO> performComparison(
            @Valid @RequestBody QuantityInputDTO quantityInputDTO) {

        logger.info("POST /compare called");
        QuantityMeasurementDTO result = service.compare(
                quantityInputDTO.getThisQuantityDTO(),
                quantityInputDTO.getThatQuantityDTO()
        );
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/convert")
    @Operation(summary = "Convert quantity to target unit",
               description = "Converts thisQuantityDTO to the unit of " +
                             "thatQuantityDTO")
    public ResponseEntity<QuantityMeasurementDTO> performConversion(
            @Valid @RequestBody QuantityInputDTO quantityInputDTO) {

        logger.info("POST /convert called");
        QuantityMeasurementDTO result = service.convert(
                quantityInputDTO.getThisQuantityDTO(),
                quantityInputDTO.getThatQuantityDTO()
        );
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/add")
    @Operation(summary = "Add two quantities",
               description = "Adds two quantities. Result is in the unit " +
                             "of thisQuantityDTO")
    public ResponseEntity<QuantityMeasurementDTO> performAddition(
            @Valid @RequestBody QuantityInputDTO quantityInputDTO) {

        logger.info("POST /add called");
        QuantityMeasurementDTO result = service.add(
                quantityInputDTO.getThisQuantityDTO(),
                quantityInputDTO.getThatQuantityDTO()
        );
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/add-with-target-unit")
    @Operation(summary = "Add two quantities with a target unit",
               description = "Adds two quantities and expresses the result " +
                             "in the unit of targetQuantityDTO")
    public ResponseEntity<QuantityMeasurementDTO> performAdditionWithTargetUnit(
            @Valid @RequestBody QuantityInputDTO quantityInputDTO) {

        logger.info("POST /add-with-target-unit called");

        // targetQuantityDTO is optional — fall back to thisQuantityDTO if absent
        QuantityMeasurementDTO result;
        if (quantityInputDTO.getTargetQuantityDTO() != null) {
            result = service.add(
                    quantityInputDTO.getThisQuantityDTO(),
                    quantityInputDTO.getThatQuantityDTO(),
                    quantityInputDTO.getTargetQuantityDTO()
            );
        } else {
            result = service.add(
                    quantityInputDTO.getThisQuantityDTO(),
                    quantityInputDTO.getThatQuantityDTO()
            );
        }
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/subtract")
    @Operation(summary = "Subtract two quantities",
               description = "Subtracts thatQuantityDTO from thisQuantityDTO. " +
                             "Result is in the unit of thisQuantityDTO")
    public ResponseEntity<QuantityMeasurementDTO> performSubtraction(
            @Valid @RequestBody QuantityInputDTO quantityInputDTO) {

        logger.info("POST /subtract called");
        QuantityMeasurementDTO result = service.subtract(
                quantityInputDTO.getThisQuantityDTO(),
                quantityInputDTO.getThatQuantityDTO()
        );
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/subtract-with-target-unit")
    @Operation(summary = "Subtract two quantities with a target unit",
               description = "Subtracts and expresses the result in the unit " +
                             "of targetQuantityDTO")
    public ResponseEntity<QuantityMeasurementDTO> performSubtractionWithTargetUnit(
            @Valid @RequestBody QuantityInputDTO quantityInputDTO) {

        logger.info("POST /subtract-with-target-unit called");

        QuantityMeasurementDTO result;
        if (quantityInputDTO.getTargetQuantityDTO() != null) {
            result = service.subtract(
                    quantityInputDTO.getThisQuantityDTO(),
                    quantityInputDTO.getThatQuantityDTO(),
                    quantityInputDTO.getTargetQuantityDTO()
            );
        } else {
            result = service.subtract(
                    quantityInputDTO.getThisQuantityDTO(),
                    quantityInputDTO.getThatQuantityDTO()
            );
        }
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/multiply")
    @Operation(summary = "Multiply two quantities",
               description = "Multiplies two quantities. Result is in the " +
                             "unit of thisQuantityDTO")
    public ResponseEntity<QuantityMeasurementDTO> performMultiplication(
            @Valid @RequestBody QuantityInputDTO quantityInputDTO) {

        logger.info("POST /multiply called");
        QuantityMeasurementDTO result = service.multiply(
                quantityInputDTO.getThisQuantityDTO(),
                quantityInputDTO.getThatQuantityDTO()
        );
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/divide")
    @Operation(summary = "Divide two quantities",
               description = "Divides thisQuantityDTO by thatQuantityDTO. " +
                             "Returns 500 if divisor is zero")
    public ResponseEntity<QuantityMeasurementDTO> performDivision(
            @Valid @RequestBody QuantityInputDTO quantityInputDTO) {

        logger.info("POST /divide called");
        QuantityMeasurementDTO result = service.divide(
                quantityInputDTO.getThisQuantityDTO(),
                quantityInputDTO.getThatQuantityDTO()
        );
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/history/operation/{operation}")
    @Operation(summary = "Get operation history",
               description = "Valid operations: ADD, SUBTRACT, MULTIPLY, " +
                             "DIVIDE, COMPARE, CONVERT")
    public ResponseEntity<List<QuantityMeasurementDTO>> getOperationHistory(
            @PathVariable String operation) {

        logger.info("GET /history/operation/" + operation + " called");
        List<QuantityMeasurementDTO> history =
                service.getOperationHistory(operation);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/history/type/{type}")
    @Operation(summary = "Get operation history by type",
               description = "Valid types: LengthUnit, VolumeUnit, " +
                             "WeightUnit, TemperatureUnit")
    public ResponseEntity<List<QuantityMeasurementDTO>> getOperationHistoryByType(
            @PathVariable String type) {

        logger.info("GET /history/type/" + type + " called");
        List<QuantityMeasurementDTO> history =
                service.getMeasurementsByType(type);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/count/{operation}")
    @Operation(summary = "Get operation count",
               description = "Counts successful (non-error) operations " +
                             "of the specified type")
    public ResponseEntity<Long> getOperationCount(
            @PathVariable String operation) {

        logger.info("GET /count/" + operation + " called");
        long count = service.getOperationCount(operation);
        return ResponseEntity.ok(count);
    }

    
    @GetMapping("/history/errored")
    @Operation(summary = "Get errored operations history",
               description = "Returns all operations that resulted in errors")
    public ResponseEntity<List<QuantityMeasurementDTO>> getErroredOperations() {

        logger.info("GET /history/errored called");
        List<QuantityMeasurementDTO> errors = service.getErrorHistory();
        return ResponseEntity.ok(errors);
    }

	@GetMapping("/")
    public String home(){
        return "Application Running Properly!";
    }
	
}
