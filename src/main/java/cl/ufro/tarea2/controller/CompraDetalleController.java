package cl.ufro.tarea2.controller;

import cl.ufro.tarea2.model.Compra;
import cl.ufro.tarea2.repository.CompraRepository;
import cl.ufro.tarea2.service.CompraService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
public class CompraDetalleController {

    private final CompraRepository compraRepository;
    private final CompraService compraService;

    public CompraDetalleController(CompraRepository compraRepository, CompraService compraService) {
        this.compraRepository = compraRepository;
        this.compraService = compraService;
    }

    @GetMapping("/compras/detalle/{id}")
    public DetalleCompra obtenerDetalle(@PathVariable("id") Integer id) {
        Compra compra = compraRepository.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe la compra con id " + id));
        CompraService.ResultadoConversion conversion = compraService.convertir(compra);

        return new DetalleCompra(compra.getId(), compra.getProducto(), compra.getMonto(),
                compra.getUnidad(), compra.getFecha(), conversion.valorUnidad(), conversion.montoPesos());
    }

    public record DetalleCompra(
            Integer id,
            String producto,
            BigDecimal monto,
            String unidad,
            LocalDate fecha,
            @JsonProperty("valor_unidad") BigDecimal valorUnidad,
            @JsonProperty("monto_pesos") long montoPesos
    ) {
    }
}
