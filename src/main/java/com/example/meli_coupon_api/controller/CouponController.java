package com.example.meli_coupon_api.controller;

import com.example.meli_coupon_api.model.CouponRequest;
import com.example.meli_coupon_api.model.CouponResponse;
import com.example.meli_coupon_api.model.ErrorResponse;
import com.example.meli_coupon_api.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupon")
@Tag(name = "Cupón", description = "API para el cálculo de ítems óptimos a comprar con un cupón")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Operation(
            summary = "Calcula los ítems óptimos para comprar con un cupón",
            description = "Dado un conjunto de ítems favoritos y un monto de cupón, " +
                    "devuelve la combinación óptima de ítems que maximiza el uso del cupón"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cálculo exitoso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CouponResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró una combinación válida de ítems",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida - Datos de entrada incorrectos",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno - Error del servicio",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping(
            value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getCouponItems(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos del cupón y lista de ítems favoritos",
                    content = @Content(
                            schema = @Schema(implementation = CouponRequest.class)
                    )
            )
            @RequestBody CouponRequest request
    ) {
        if (request == null || request.getItem_ids() == null || request.getItem_ids().isEmpty() || request.getAmount() == null || request.getAmount() <= 0) {
            ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Los datos de entrada son inválidos.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        try {
        CouponResponse response = couponService.calculateOptimalItems(request.getItem_ids(), request.getAmount());
        if (response == null || response.getItem_ids().isEmpty()) {
            ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "No se puede comprar ningún ítem con la información proporcionado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Hubo un error al procesar la solicitud.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
