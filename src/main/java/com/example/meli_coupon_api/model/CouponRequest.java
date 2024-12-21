package com.example.meli_coupon_api.model;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Schema(description = "Objeto de solicitud para el cálculo de cupón")
public class CouponRequest {
    @Schema(
            description = "Conjunto de IDs de items favoritos",
            example = "[\"MLA1\", \"MLA2\", \"MLA3\"]",
            required = true
    )
    private Set<String> item_ids;
    @Schema(
            description = "Monto del cupón",
            example = "500.0",
            required = true
    )
    private Float amount;


    public Set<String> getItem_ids() {
        return item_ids;
    }

    public void setItem_ids(Set<String> item_ids) {
        this.item_ids = item_ids;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
