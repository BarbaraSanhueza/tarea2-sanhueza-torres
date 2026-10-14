package cl.ufro.tarea2.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import tools.jackson.databind.JsonNode;

@Service
public class MindicadorService {

    private final RestClient restClient = RestClient.create();
    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public BigDecimal obtenerValor(String unidad, LocalDate fecha) {
        String indicador = switch (unidad.trim().toUpperCase(Locale.ROOT)) {
            case "UF" -> "uf";
            case "USD", "DOLAR" -> "dolar";
            default -> throw new IllegalArgumentException(
                    "Unidad no válida: " + unidad
            );
        };

        String fechaApi = fecha.format(formatoFecha);

        JsonNode respuesta = restClient.get()
                .uri(
                        "https://mindicador.cl/api/{indicador}/{fecha}",
                        indicador,
                        fechaApi
                )
                .retrieve()
                .body(JsonNode.class);

        JsonNode serie = respuesta == null
                ? null
                : respuesta.path("serie");

        if (serie == null || !serie.isArray() || serie.size() == 0) {
            throw new IllegalStateException(
                    "No hay un valor disponible para esa fecha"
            );
        }

        JsonNode valor = serie.get(0).path("valor");

        if (!valor.isNumber()) {
            throw new IllegalStateException(
                    "La respuesta de mindicador no contiene un valor válido"
            );
        }

        return valor.decimalValue();
    }
}