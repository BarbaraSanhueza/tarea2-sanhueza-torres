# Tarea 2 - Ingeniería de Software

API REST desarrollada para la Tarea 2 de Ingeniería de Software. Lee las compras desde `compras.csv`, consulta los valores históricos de UF y dólar en mindicador.cl y entrega los montos convertidos a pesos chilenos.

## Tecnologías

- Java 21
- Spring Boot 4.1.1
- Maven
- Spring Web
- Spring Boot Starter Test

## Integrantes

| Integrante | Usuario de GitHub | Desafío asignado |
| --- | --- | --- |
| Sanhueza | `BarbaraSanhueza` | Desafíos 1 y 4 |
| Torres | `DtorresUfro` | Desafíos 2 y 3 |

## Endpoints

| Método | Ruta | Descripción |
| --- | --- | --- |
| `GET` | `/compras/{unidad}` | Lista y totaliza las compras de la unidad `uf` o `dolar`. |
| `GET` | `/compras/detalle/{id}` | Entrega el detalle de una compra y su monto convertido a pesos. |

La aplicación usa el puerto `3001` de forma predeterminada. El archivo `compras.csv` permanece en la raíz del repositorio y, al ejecutar con Docker, se monta como archivo externo de solo lectura en `/data/compras.csv`.

## Ejecución con Docker Compose

```bash
docker compose up --build
```

Variables disponibles:

- `APP_PORT`: puerto de la aplicación; valor predeterminado `3001`.
- `APP_ENV`: entorno de ejecución; en Docker se configura como `production`.
- `CSV_PATH`: ruta al CSV en el equipo anfitrión; valor predeterminado `./compras.csv` para Docker Compose.

## Pipeline

GitHub Actions ejecuta los trabajos `test`, `build` y `publish` en ese orden. La publicación se realiza únicamente al enviar cambios a `main` y genera una imagen en GHCR con las etiquetas `latest` y los primeros siete caracteres del SHA del commit.

## Ramas

- `main`
- `develop`
- `docker`
- `pipeline`
