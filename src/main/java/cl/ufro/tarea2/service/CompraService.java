package cl.ufro.tarea2.service;

import cl.ufro.tarea2.model.Compra;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CompraService {

    private final MindicadorService mindicadorService;

    public CompraService(MindicadorService mindicadorService) {
        this.mindicadorService = mindicadorService;
    }

    public ResultadoConversion convertir(Compra compra) {
        BigDecimal valorUnidad = mindicadorService.obtenerValor(
                compra.getUnidad(),
                compra.getFecha()
        );

        long montoPesos = compra.getMonto()
                .multiply(valorUnidad)
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();

        return new ResultadoConversion(valorUnidad, montoPesos);
    }

    public record ResultadoConversion(
            BigDecimal valorUnidad,
            long montoPesos
    ) {
    }
}