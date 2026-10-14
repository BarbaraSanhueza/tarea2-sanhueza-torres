package cl.ufro.tarea2.controlador;

import cl.ufro.tarea2.model.Compra;
import cl.ufro.tarea2.model.CompraConvertida;
import cl.ufro.tarea2.model.CompraUnidadResponse;
import cl.ufro.tarea2.repository.CompraRepository;
import cl.ufro.tarea2.service.CompraService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/compras")
public class ComprasUnidadController {

    private final CompraRepository compraRepository;
    private final CompraService compraService;

    public ComprasUnidadController(CompraRepository compraRepository, CompraService compraService) {
        this.compraRepository = compraRepository;
        this.compraService = compraService;
    }

    @GetMapping("/{unidad}")
    public CompraUnidadResponse obtenerComprasPorUnidad(
            @PathVariable String unidad) {

        String unidadBuscada;

        if (unidad.equalsIgnoreCase("uf")) {
            unidadBuscada = "UF";
        } else if (unidad.equalsIgnoreCase("dolar")) {
            unidadBuscada = "USD";
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unidad no válida"
            );
        }

        List<Compra> compras = compraRepository.obtenerTodas();
        List<CompraConvertida> comprasConvertidas = new ArrayList<>();
        long totalPesos = 0;

        for (Compra compra : compras) {
            if (compra.getUnidad().equalsIgnoreCase(unidadBuscada)) {
                CompraService.ResultadoConversion conversion =
                        compraService.convertir(compra);

                comprasConvertidas.add(new CompraConvertida(
                        compra.getId(),
                        compra.getProducto(),
                        compra.getMonto(),
                        compra.getFecha(),
                        conversion.valorUnidad(),
                        conversion.montoPesos()
                ));

                totalPesos += conversion.montoPesos();
            }
        }

        return new CompraUnidadResponse(
                unidadBuscada,
                comprasConvertidas,
                totalPesos
        );
    }
}
