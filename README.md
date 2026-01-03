# 🧬 Mutant Detector API

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-green?style=flat-square&logo=springboot)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue?style=flat-square&logo=docker)
![Coverage](https://img.shields.io/badge/Coverage-90%25-brightgreen?style=flat-square)

> **Trabajo Integrador Desarrollo de Software** > API REST diseñada para detectar mutantes basándose en su secuencia de ADN mediante análisis de matrices.

## 📋 Tabla de Contenidos
- [El Problema](#-el-problema)
- [Tecnologías](#-tecnologías)
- [Arquitectura](#-arquitectura)
- [API Endpoints](#-api-endpoints)
- [Ejecución Local](#-ejecución-local)
- [Docker](#-despliegue-con-docker)
- [Testing](#-testing)
- [Optimizaciones y Rendimiento](#-optimizaciones-implementadas)

---

## 🎯 El Problema

Magneto quiere reclutar mutantes para su ejército. Este sistema detecta si un humano es mutante analizando su secuencia de ADN.

Un humano es **mutante** si se encuentran **más de una secuencia de 4 letras iguales** (A, T, C, G) en direcciones:
* Horizontal
* Vertical
* Diagonal

### Representación
El ADN se recibe como un array de Strings que representa una matriz NxN:

```json
{
  "dna": [
    "ATGCGA",
    "CAGTGC",
    "TTATGT",
    "AGAAGG",
    "CCCCTA",
    "TCACTG"
  ]
}

```

*En este caso, se encuentra una secuencia horizontal (`CCCC`) y una diagonal (`AAAA`), por lo que es un mutante.*

---

## 🛠 Tecnologías

* **Lenguaje:** Java 17
* **Framework:** Spring Boot 3.2.0
* **Base de Datos:** H2 Database (In-Memory)
* **Persistencia:** Spring Data JPA
* **Documentación:** OpenAPI (Swagger)
* **Testing:** JUnit 5, MockMvc, JaCoCo
* **Contenedorización:** Docker
* **Utilidades:** Lombok, Gradle

---

## 🏗 Arquitectura

El proyecto sigue una arquitectura en capas clásica para asegurar la separación de responsabilidades:

1. **Controller Layer:** Maneja las peticiones HTTP y validaciones de entrada (`@Validated`).
2. **Service Layer:** Contiene la lógica de negocio, el algoritmo de detección y la gestión de caché.
3. **Repository Layer:** Interactúa con la base de datos usando JPA.
4. **Database:** Almacena los resultados de los análisis para evitar reprocesamientos.

---

## 📡 API Endpoints

Documentación interactiva disponible en: `http://localhost:8080/swagger-ui.html`

### 1. Detectar Mutante

Verifica si una secuencia de ADN corresponde a un mutante.

* **URL:** `/mutant`
* **Method:** `POST`
* **Codes:**
* `200 OK`: Es Mutante.
* `403 Forbidden`: No es Mutante.
* `400 Bad Request`: ADN inválido.



**Ejemplo de Request:**

```bash
curl -X POST http://localhost:8080/mutant \
  -H "Content-Type: application/json" \
  -d '{"dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]}'

```

### 2. Estadísticas

Devuelve estadísticas de las verificaciones de ADN.

* **URL:** `/stats`
* **Method:** `GET`
* **Response:**

```json
{
    "count_mutant_dna": 40,
    "count_human_dna": 100,
    "ratio": 0.4
}

```

---

## 🚀 Ejecución Local

### Prerrequisitos

* Java 17+
* Git

### Pasos

1. **Clonar el repositorio:**
```bash
git clone [https://github.com/TU-USUARIO/mutantes.git](https://github.com/TU-USUARIO/mutantes.git)
cd mutantes

```


2. **Ejecutar la aplicación:**
```bash
# Windows
./gradlew.bat bootRun

# Mac/Linux
./gradlew bootRun

```



La API estará disponible en `http://localhost:8080`.

---

## 🐳 Despliegue con Docker

El proyecto incluye un `Dockerfile` optimizado (Multi-stage build) basado en Alpine Linux.

1. **Construir la imagen:**
```bash
docker build -t mutantes-api .

```


2. **Ejecutar el contenedor:**
```bash
docker run -p 8080:8080 mutantes-api

```



---

## 🧪 Testing

El proyecto cuenta con una cobertura de código superior al **90%**, incluyendo tests unitarios y de integración.

Para ejecutar los tests y generar el reporte de cobertura:

```bash
./gradlew test jacocoTestReport

```

El reporte HTML se generará en `build/reports/jacoco/test/html/index.html`.

---

## ⚡ Optimizaciones Implementadas

Dado que se trata de un servicio que debe soportar alta concurrencia, se implementaron varias estrategias de optimización:

1. **Early Termination (Terminación Temprana):**
El algoritmo se detiene inmediatamente apenas encuentra la segunda secuencia válida. Esto mejora el rendimiento promedio de O(N²) a casi O(N) en casos positivos.
2. **Caché de Base de Datos:**
Antes de analizar una matriz, se genera un **Hash SHA-256** del ADN y se busca en la base de datos.
* Si ya existe, se devuelve el resultado previo (O(1)).
* Esto evita re-procesar ADNs ya conocidos.


3. **Índices SQL:**
Se crearon índices en las columnas `dna_hash` e `is_mutant` para garantizar búsquedas y conteos estadísticos instantáneos incluso con millones de registros.
4. **Validación Fail-Fast:**
Las validaciones de formato (NxN, caracteres válidos) se realizan en el DTO antes de llegar a la capa de servicio, ahorrando ciclos de CPU en peticiones inválidas.

