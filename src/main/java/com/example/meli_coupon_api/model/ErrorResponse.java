package com.example.meli_coupon_api.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class ErrorResponse {
    private int code;
    private String message;
    private String details;

    // Constructor
    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // Constructor con detalles adicionales
    public ErrorResponse(int code, String message, String details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }

    // Getters y setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
