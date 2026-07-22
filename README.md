# Joy For Living Watersports & Activities Â· AplicaciÃ³n web de gestiÃ³n

AplicaciÃ³n web full-stack construida con **Spring Boot 3 / Java 21** en el back-end y **React 18 + Vite** en el front-end, que digitaliza el mapa de capacidades de la microempresa turÃ­stica Joy For Living Watersports & Activities.

[![IntegraciÃ³n continua](https://github.com/stamperbenjamin7-web/joy-for-living/actions/workflows/ci.yml/badge.svg)](https://github.com/stamperbenjamin7-web/joy-for-living/actions/workflows/ci.yml)

---

## 1. InformaciÃ³n del estudiante

| Campo | Detalle |
|---|---|
| **Nombre** | Benjamin Jair Stamper Ãlvarez |
| **Universidad** | Universidad TÃ©cnica Particular de Loja (UTPL) |
| **Carrera** | TecnologÃ­a Superior en TransformaciÃ³n Digital de Empresas |
| **Asignatura** | Desarrollo de aplicaciones web |
| **Unidad** | Unidad 3 â€” Desarrollo de Back-End con Spring y Java |
| **Recurso** | Taller: Desarrollo de una aplicaciÃ³n web en React y Spring |
| **Periodo acadÃ©mico** | *(completar)* |

---

## 2. InformaciÃ³n de la empresa

**Joy For Living Watersports & Activities** es una microempresa del sector turÃ­stico ubicada en **Noord, Aruba**, dedicada a la operaciÃ³n de experiencias acuÃ¡ticas para visitantes internacionales: snorkel guiado, kayak transparente en manglares, paddle board, motos acuÃ¡ticas, buceo y salidas en catamarÃ¡n al atardecer.

### SituaciÃ³n de partida (AS-IS)

La operaciÃ³n se gestionaba con canales fragmentados: reservas por WhatsApp y correo, agenda en hojas de cÃ¡lculo compartidas y control de equipamiento en registros manuales. Esto producÃ­a sobreventa de cupos, pÃ©rdida de trazabilidad de las reservas y ausencia de indicadores para la toma de decisiones.

### SituaciÃ³n objetivo (TO-BE)

Una plataforma Ãºnica que centraliza el catÃ¡logo de experiencias, valida automÃ¡ticamente la disponibilidad de cupos, mantiene el registro de clientes, controla el inventario de equipamiento y expone indicadores operativos en tiempo real.

---

## 3. Mapa de capacidades

El mapa de capacidades levantado durante el PrÃ¡cticum 3 y modelado en ArchiMate (Unidad 2) es el que esta aplicaciÃ³n implementa. Las capacidades sombreadas son las que el software cubre directamente.

```mermaid
graph TD
    A[Joy For Living Watersports & Activities]

    A --> B[GestiÃ³n comercial]
    A --> C[OperaciÃ³n de experiencias]
    A --> D[GestiÃ³n de recursos]
    A --> E[Inteligencia de negocio]

    B --> B1[CatÃ¡logo de experiencias]
    B --> B2[GestiÃ³n de reservas]
    B --> B3[GestiÃ³n de clientes]

    C --> C1[ProgramaciÃ³n de salidas]
    C --> C2[Control de disponibilidad y cupos]
    C --> C3[Ciclo de vida de la reserva]

    D --> D1[Inventario de equipamiento]
    D --> D2[Control de mantenimiento]

    E --> E1[Indicadores operativos]
    E --> E2[AnÃ¡lisis de demanda por categorÃ­a]

    style B1 fill:#17b0bd,color:#06263f
    style B2 fill:#17b0bd,color:#06263f
    style B3 fill:#17b0bd,color:#06263f
    style C1 fill:#17b0bd,color:#06263f
    style C2 fill:#17b0bd,color:#06263f
    style C3 fill:#17b0bd,color:#06263f
    style D1 fill:#17b0bd,color:#06263f
    style D2 fill:#17b0bd,color:#06263f
    style E1 fill:#17b0bd,color:#06263f
    style E2 fill:#17b0bd,color:#06263f
```

### Trazabilidad capacidad â†’ componente de software

| Capacidad de negocio | MÃ³dulo de la aplicaciÃ³n | Endpoints |
|---|---|---|
| CatÃ¡logo de experiencias | `ActividadService`, pantalla *Experiencias* | `/api/actividades` |
| GestiÃ³n de clientes | `ClienteService`, pantalla *Clientes* | `/api/clientes` |
| GestiÃ³n de reservas y ciclo de vida | `ReservaService`, pantalla *Reservas* | `/api/reservas` |
| Control de disponibilidad y cupos | `ReservaService.consultarDisponibilidad` | `/api/reservas/disponibilidad` |
| Inventario y mantenimiento de equipamiento | `EquipoService`, pantalla *Equipamiento* | `/api/equipos` |
| Indicadores y anÃ¡lisis de demanda | `ReporteService`, pantalla *Panel* | `/api/reportes/resumen` |

---

## 4. AplicaciÃ³n objetivo

### Alcance funcional

- **Panel de operaciones.** Ingresos confirmados, reservas por estado, prÃ³ximas cinco salidas y demanda agrupada por categorÃ­a de experiencia.
- **Experiencias.** Alta, consulta y baja lÃ³gica del catÃ¡logo, con duraciÃ³n, cupo mÃ¡ximo por salida, tarifa por persona y punto de encuentro.
- **Reservas.** CreaciÃ³n con validaciÃ³n de cupos en tiempo real, cÃ¡lculo automÃ¡tico del importe, cÃ³digo legible (`JFL-XXXXXX`) y transiciones de estado controladas: `PENDIENTE â†’ CONFIRMADA â†’ COMPLETADA`, con cancelaciÃ³n posible desde los dos primeros estados.
- **Clientes.** Registro con correo Ãºnico y bÃºsqueda por nombre, apellido o correo.
- **Equipamiento.** Inventario con unidades comprometidas frente al total y fecha del Ãºltimo mantenimiento.

### Reglas de negocio implementadas

1. No se puede reservar por encima de la capacidad mÃ¡xima de la salida. Dos salidas de la misma experiencia se consideran solapadas cuando la diferencia entre sus horas de inicio es menor que la duraciÃ³n de la actividad.
2. Las reservas canceladas no consumen cupo.
3. El importe se calcula como `precio por persona Ã— nÃºmero de personas`.
4. Una reserva `CANCELADA` o `COMPLETADA` es terminal: no admite mÃ¡s transiciones.
5. Las unidades disponibles de un equipo nunca pueden superar el total registrado.
6. Retirar una experiencia del catÃ¡logo es una baja lÃ³gica, para preservar el histÃ³rico de reservas.

### Arquitectura

```
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚  Navegador                                                   â”‚
â”‚  React 18 + Vite + React Router                              â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
                            â”‚  fetch â†’ /api
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â–¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚  Spring Boot 3.3 (Java 21)                                   â”‚
â”‚  Controller  â†’  Service (reglas de negocio)  â†’  Repository   â”‚
â”‚  Bean Validation Â· Manejo global de errores Â· OpenAPI        â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
                            â”‚  Spring Data JPA / Hibernate
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â–¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚  H2 en memoria (perfil dev)   Â·   PostgreSQL (perfil prod)   â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
```

El front-end construido por Vite se copia a `target/classes/static` durante el empaquetado, de modo que **un solo artefacto `.jar` contiene la aplicaciÃ³n completa**.

### Modelo de datos

| Entidad | Atributos principales | Relaciones |
|---|---|---|
| `Actividad` | nombre, categorÃ­a, duraciÃ³n, precio, capacidad, punto de encuentro | 1 â†’ N `Reserva`, 1 â†’ N `Equipo` |
| `Cliente` | nombre, apellido, email (Ãºnico), telÃ©fono, paÃ­s | 1 â†’ N `Reserva` |
| `Reserva` | cÃ³digo, fecha y hora, personas, total, estado, notas | N â†’ 1 `Cliente`, N â†’ 1 `Actividad` |
| `Equipo` | nombre, tipo, cantidad total y disponible, estado, mantenimiento | N â†’ 1 `Actividad` |

---

## 5. TecnologÃ­as

| Capa | TecnologÃ­a |
|---|---|
| Back-end | Java 21, Spring Boot 3.3.4, Spring Web, Spring Data JPA, Bean Validation, Spring Actuator |
| Front-end | React 18.3, Vite 5, React Router 6 |
| Base de datos | H2 (desarrollo), PostgreSQL (producciÃ³n) |
| DocumentaciÃ³n de API | springdoc-openapi (Swagger UI) |
| ConstrucciÃ³n | Maven 3.9, frontend-maven-plugin |
| Pruebas | JUnit 5, Mockito, AssertJ |
| IntegraciÃ³n continua | GitHub Actions |
| Despliegue | Docker multietapa sobre Railway |

---

## 6. Requisitos previos

- **JDK 21** o superior
- **Maven 3.9+** (o el wrapper `./mvnw`)
- **Node.js 20+** y npm â€” solo si vas a trabajar el front-end por separado; el empaquetado con Maven descarga su propia copia de Node.

Verifica tu entorno:

```bash
java -version
mvn -version
node -v
```

---

## 7. InstalaciÃ³n y ejecuciÃ³n

### 7.1 Clonar el repositorio

```bash
git clone https://github.com/stamperbenjamin7-web/joy-for-living.git
cd joy-for-living
```

### 7.2 OpciÃ³n A â€” EjecuciÃ³n en un solo comando (recomendada)

Maven construye el front-end, lo empaqueta dentro del JAR y levanta todo:

```bash
mvn clean package
java -jar target/joy-for-living-api-1.0.0.jar
```

Abre **http://localhost:8080**. El perfil `dev` carga automÃ¡ticamente datos de demostraciÃ³n: cinco experiencias, cuatro clientes, cinco equipos y tres reservas.

### 7.3 OpciÃ³n B â€” Desarrollo con recarga en caliente

Dos terminales:

```bash
# Terminal 1 â€” back-end en el puerto 8080
mvn spring-boot:run -DskipFrontend

# Terminal 2 â€” front-end en el puerto 5173 con proxy hacia /api
cd frontend
npm install
npm run dev
```

Abre **http://localhost:5173**.

### 7.4 Ejecutar las pruebas

```bash
mvn test
```

Se ejecutan las pruebas unitarias de las reglas de reservas (cÃ¡lculo de importes, bloqueo por falta de cupos, disponibilidad y transiciones de estado) y la prueba de humo del contexto de Spring.

---

## 8. Uso de la aplicaciÃ³n

| Pantalla | QuÃ© hacer |
|---|---|
| **Panel** | Revisar los indicadores del dÃ­a y la agenda de prÃ³ximas salidas. |
| **Experiencias** | Publicar una nueva experiencia con *Publicar experiencia*, o retirar una del catÃ¡logo activo. |
| **Reservas** | Elegir cliente, experiencia y horario. La barra de marea muestra los cupos comprometidos antes de confirmar. Desde la tabla se confirma, completa o cancela cada reserva. |
| **Clientes** | Registrar visitantes y buscarlos por nombre o correo. |
| **Equipamiento** | Consultar unidades en uso y fechas de mantenimiento. |

### Herramientas de desarrollo

| Recurso | URL |
|---|---|
| DocumentaciÃ³n interactiva de la API | http://localhost:8080/swagger-ui.html |
| EspecificaciÃ³n OpenAPI | http://localhost:8080/v3/api-docs |
| Consola H2 (perfil dev) | http://localhost:8080/h2-console â€” JDBC `jdbc:h2:mem:joyforliving`, usuario `sa`, sin contraseÃ±a |
| Estado del servicio | http://localhost:8080/actuator/health |

### Ejemplos de consumo de la API

```bash
# CatÃ¡logo de experiencias
curl http://localhost:8080/api/actividades

# Cupos disponibles de una experiencia en un horario
curl "http://localhost:8080/api/reservas/disponibilidad?actividadId=1&fechaHora=2026-08-15T09:00:00"

# Crear una reserva
curl -X POST http://localhost:8080/api/reservas \
  -H "Content-Type: application/json" \
  -d '{"clienteId":1,"actividadId":1,"fechaHora":"2026-08-15T09:00:00","numeroPersonas":2,"notas":"Primera vez"}'

# Confirmar una reserva
curl -X PATCH "http://localhost:8080/api/reservas/1/estado?valor=CONFIRMADA"

# Indicadores del panel
curl http://localhost:8080/api/reportes/resumen
```

---

## 9. IntegraciÃ³n continua y automatizaciÃ³n de la construcciÃ³n

El repositorio incluye un flujo de trabajo de GitHub Actions (`.github/workflows/ci.yml`) que se dispara en cada `push` y cada *pull request* sobre `main`:

1. **Descarga del cÃ³digo** y preparaciÃ³n de JDK 21 y Node 20, con cachÃ© de dependencias de Maven y npm.
2. **ConstrucciÃ³n del front-end** (`npm install` + `npm run build`).
3. **CompilaciÃ³n y pruebas del back-end** (`mvn verify`).
4. **PublicaciÃ³n de artefactos**: el reporte de pruebas y el `.jar` ejecutable quedan disponibles para descarga desde la ejecuciÃ³n del flujo.

La automatizaciÃ³n del empaquetado se apoya en `frontend-maven-plugin`: durante la fase `generate-resources`, Maven descarga Node, instala las dependencias del front-end y ejecuta la construcciÃ³n de Vite; luego `maven-resources-plugin` copia `frontend/dist` a `target/classes/static`. El resultado es un Ãºnico artefacto desplegable, sin pasos manuales.

---

## 10. Despliegue en Railway

**AplicaciÃ³n desplegada:** *(pegar aquÃ­ la URL que entregue Railway, por ejemplo `https://joy-for-living-production.up.railway.app`)*

Pasos:

1. Crear un proyecto en [railway.app](https://railway.app) y elegir **Deploy from GitHub repo**, seleccionando este repositorio.
2. AÃ±adir el servicio **PostgreSQL** desde *New â†’ Database â†’ Add PostgreSQL*.
3. En el servicio de la aplicaciÃ³n, secciÃ³n **Variables**, definir:

   | Variable | Valor |
   |---|---|
   | `SPRING_PROFILES_ACTIVE` | `prod` |
   | `JDBC_DATABASE_URL` | `jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}` |
   | `PGUSER` | `${{Postgres.PGUSER}}` |
   | `PGPASSWORD` | `${{Postgres.PGPASSWORD}}` |

4. Railway detecta el `Dockerfile` y construye la imagen multietapa. La variable `PORT` la inyecta la plataforma y la aplicaciÃ³n la lee automÃ¡ticamente.
5. En *Settings â†’ Networking*, generar el dominio pÃºblico y copiarlo en este README.

El *healthcheck* configurado en `railway.json` apunta a `/actuator/health`.

---

## 11. Estructura del proyecto

```
joy-for-living/
â”œâ”€â”€ .github/workflows/ci.yml         Flujo de integraciÃ³n continua
â”œâ”€â”€ Dockerfile                       Imagen multietapa para Railway
â”œâ”€â”€ railway.json                     ConfiguraciÃ³n de despliegue
â”œâ”€â”€ pom.xml                          ConstrucciÃ³n unificada back + front
â”œâ”€â”€ frontend/                        AplicaciÃ³n React
â”‚   â”œâ”€â”€ src/
â”‚   â”‚   â”œâ”€â”€ components/              Marea, Distintivo, Olas
â”‚   â”‚   â”œâ”€â”€ pages/                   Panel, Experiencias, Reservas, Clientes, Equipos
â”‚   â”‚   â”œâ”€â”€ api.js                   Cliente HTTP del API
â”‚   â”‚   â”œâ”€â”€ App.jsx                  Rutas y estructura
â”‚   â”‚   â””â”€â”€ styles.css               Sistema visual
â”‚   â””â”€â”€ vite.config.js
â””â”€â”€ src/
    â”œâ”€â”€ main/java/com/joyforliving/api/
    â”‚   â”œâ”€â”€ domain/                  Entidades JPA
    â”‚   â”œâ”€â”€ repository/              Spring Data
    â”‚   â”œâ”€â”€ service/                 Reglas de negocio
    â”‚   â”œâ”€â”€ controller/              Endpoints REST
    â”‚   â”œâ”€â”€ dto/                     Contratos de entrada y salida
    â”‚   â”œâ”€â”€ exception/               Manejo global de errores
    â”‚   â””â”€â”€ config/                  CORS, OpenAPI, datos de demostraciÃ³n
    â”œâ”€â”€ main/resources/              application.yml, dev y prod
    â””â”€â”€ test/java/                   Pruebas unitarias y de contexto
```

---

## 12. Licencia

Proyecto acadÃ©mico desarrollado para la Universidad TÃ©cnica Particular de Loja. Uso educativo.
#   j o y - f o r - l i v i n g - a p p 
 
