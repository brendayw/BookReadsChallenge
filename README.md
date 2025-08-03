# LiterAlura Challenge

Practicando Spring Boot: challenge de literatura para Alura Latam - programa ONE

## Tecnologías Utilizadas
- Java 17.0
- Spring Boot
- Maven
- Spring Data JPA
- Jackson
- PostgreSQL

## Instalacion

1. Clonar el repositorio:
        
        git clone https://github.com/brendayw/BookReadsChallenge
        cd BookReadsChallenge
  
2. Configurar PostgreSQL:
   
       Asegúrate de tener una instancia de PostgreSQL en ejecución.
       Crea una base de datos llamada alura_libros.
       Verifica las credenciales de base de datos (username, password) en application.properties y ajustarlas si es necesario.

5. Compilar y Ejecutar:

       mvn spring-boot:run


## Estructura

    BookReads/
      ├───dto      
            ├─── AutorDTO.java
            └─── LibroDTO.java
      ├───model
            ├───exceptions
                    ├─── LibroExistenteException.java
                    └─── LibroNoEncontradoException.java
            ├─── Datos.java
            ├─── DatosAutor.java
            ├─── DatosLibro.java
            ├─── Autor.java
            ├─── Libro.java
      ├───principal
            └─── Principal.java             
      ├───repository
            ├─── AutorRepository.java
            └─── LibroRepository.java
      └───service
            ├─── api
                    ├─── ConsumoAPI.java
                    ├─── ConvierteDatos.java
                    └─── IConvierteDatos.java
            ├─── AutorService.java
            ├─── ApiService.java
            └─── LibroService.java

## Estructura detallada

### DTO

- AutorDTO: representa un autor, conteniendo su ID, nombre, fechas de nacimiento/fallecimiento y una lista de libros asociados (usando el modelo Libro).
- LibroDTO: representa un libro, con campos como ID, título, idioma, número de descargas y su autor (usando el modelo Autor).

### Model

#### Exceptions
- LibroExistenteException: excepción lanzada cuando se intenta crear o registrar un libro que ya existe en la base de datos.
- LibroNoEncontradoException: excepción lanzada cuando se busca un libro que no existe.

#### Models

- Datos:
    - Contenedor principal para respuestas de API.
    - Mapea una lista de DatosLibro bajo el campo results (usando @JsonAlias).
    - Ignora campos desconocidos en JSON (@JsonIgnoreProperties).
      
- DatosAutor:
    - Modelo para autores en respuestas de API externa.
    - Mapea campos como name → nombre, birth_year → fechaNacimiento, death_year → fechaFallecimiento, etc.

- DatosLibro:
    - Modelo para datos de libros provenientes de API externa.
    - Campos mapeados con @JsonAlias: title → titulo, authors → lista de DatosAutor, etc.
    - Útil para deserializar respuestas JSON.
  
- Autor:
    - Entidad que representa un autor en la base de datos (@Entity).
    - Campos: id (autoincremental), nombre (único), fechas de nacimiento/fallecimiento, y lista de libros (@OneToMany).
    - Incluye constructor para convertir desde DatosAutor (DTO externo) y métodos estándar (getters/setters).
      
- Libro:
    - Entidad para libros (@Entity).
    - Campos: id, título (único), idioma, numeroDescargas, y relación @ManyToOne con Autor.
    - Constructor básico y métodos de acceso.
 
### Principal

- Principal:
    - es el núcleo de la interacción con el usuario en tu aplicación BookReads.
    - Actúa como un controlador de consola que maneja el menú principal y delega operaciones a los servicios correspondientes (LibroService, AutorService, ApiService).

### Repository

- AutorRepository:
    - Extiende: JpaRepository<Autor, Long> (CRUD automático).
    - Consultas personalizadas:
        - findAllWithLibros(): Autores con sus libros (evita N+1 con JOIN FETCH).
        - findAutoresVivosConLibros(int year): Autores vivos en un año específico.
     
- LibroRepository:
    - Extiende: JpaRepository<Libro, Long>.
    - Consultas personalizadas:
        - findByTituloContainingIgnoreCase(String titulo): Búsqueda flexible por título.
        - findByIdioma(String idioma): Libros filtrados por idioma (ej: "es").

### Service

#### Api

- ConsumoAPI:
    - Hace llamadas HTTP (GET) a APIs externas.
    - Devuelve respuestas como String (JSON).
 
- ConvierteDatos:
    - Convierte JSON → Objetos Java (usando Jackson).
    - Implementa IConvierteDatos.
 
- IConvierteDatos:
    - Interfaz para deserialización de datos.
 
#### Service

- ApiService:
    - Busca libros en API externa (Gutendex) y los convierte al modelo local.
    - buscarLibroEnApi(titulo):
        - Consulta la API con el título.
        - Filtra y convierte el resultado a Libro.
        - Maneja autores desconocidos.
     - convertirDatosLibroALibro(): Adapta datos de API al modelo Libro.

- AutorService:
    - Gestiona autores en la base de datos.
    - obtenerTodosLosAutores(): Lista autores con sus libros (DTOs).
    - obtenerAutoresVivos(año): Filtra autores vivos en un año específico.
    - convierteDatos(): Transforma entidades Autor a AutorDTO.

- LibroService:
    -  Administra libros (búsqueda, guardado, filtros).
    -  buscarYGuardarLibro(titulo):
        -  Busca primero en la base de datos local.
        -  Si no existe, consulta la API y guarda el libro (y su autor si es nuevo).
        -  Lanza excepciones personalizadas (LibroExistenteException, LibroNoEncontradoException).
    -  obtenerLibrosPorIdioma(idioma): Filtra libros por idioma (ej: "es").
    -  convierteDatos(): Convierte entidades Libro a LibroDTO.

## API utilizada

    https://gutendex.com/books/

## Mejoras Futuras

### Mejoras planeadas a implementar

1. ** Controlador (Controller) para API REST**
   - Implementar endpoints organizados con Spring `@RestController`
   - Documentar API con Swagger/OpenAPI

2. ** Generar Estadísticas con DoubleSummaryStatistics**
   - Estadísticas de descargas (promedio, máximo, mínimo)
   - Cantidad de libros por idioma
   - Gráficos de datos relevantes

3. ** Top 10 Libros Más Descargados**
   - Endpoint específico para ranking de libros
   - Opción de filtrar por periodo temporal

4. ** Búsqueda Avanzada de Autores**
   - Búsqueda por nombre (ignore case)
   - Filtros combinados (por nacionalidad, periodo, etc.)

5. ** Consultas Adicionales de Autores**
   - Autores por siglo/época histórica
   - Autores con más libros publicados
   - Relación autores-vivos-por-año
  
## Autor

Brenda Yañez
- 📧 Email: brendayw97@gmail.com
- 🔗 GitHub: @brendayw
- 🌐 LinkedIn: [Brenda Yañez](https://www.linkedin.com/in/brendayw/)

