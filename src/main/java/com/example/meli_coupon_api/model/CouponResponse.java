package com.example.meli_coupon_api.model;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Schema(description = "Objeto de respuesta con el resultado del cálculo del cupón")
public class CouponResponse {
    @Schema(
            description = "Conjunto de IDs de items seleccionados para compra",
            example = "[\"MLA1\", \"MLA3\", \"MLA4\"]"
    )
    private Set<String> item_ids;

    @Schema(
            description = "Monto total de los items seleccionados",
            example = "480.5"
    )
    private Float total;

    public CouponResponse(Set<String> item_ids, Float total) {
        this.item_ids = item_ids;
        this.total = total;
    }

    public Set<String> getItem_ids() {
        return item_ids;
    }

    public void setItem_ids(Set<String> item_ids) {
        this.item_ids = item_ids;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }
}

