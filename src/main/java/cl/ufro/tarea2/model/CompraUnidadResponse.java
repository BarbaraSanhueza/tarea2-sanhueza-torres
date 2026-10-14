package cl.ufro.tarea2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CompraUnidadResponse(
        String unidad,
        List<CompraConvertida> compras,
        @JsonProperty("total_pesos")long totalPesos

) {
}
