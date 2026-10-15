package cl.ufro.tarea2.repository;

import cl.ufro.tarea2.model.Compra;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CompraRepository {

    private final Path rutaCsv;

    public CompraRepository(@Value("${CSV_PATH:compras.csv}") String rutaCsv) {
        this.rutaCsv = Path.of(rutaCsv);
    }

    public List<Compra> obtenerTodas() {
        List<Compra> compras = new ArrayList<>();

        try (BufferedReader lector = Files.newBufferedReader(rutaCsv)) {
            String linea;
            boolean primeraLinea = true;

            while ((linea = lector.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                if (linea.isBlank()) {
                    continue;
                }

                String[] datos = linea.split(",");

                Compra compra = new Compra(
                        Integer.parseInt(datos[0].trim()),
                        datos[1].trim(),
                        new BigDecimal(datos[2].trim()),
                        datos[3].trim(),
                        LocalDate.parse(datos[4].trim())
                );

                compras.add(compra);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(
                    "No se pudo leer el archivo: " + rutaCsv,
                    e
            );
        }

        return compras;
    }

    public Optional<Compra> buscarPorId(Integer id) {
        return obtenerTodas().stream()
                .filter(compra -> compra.getId().equals(id))
                .findFirst();
    }
}