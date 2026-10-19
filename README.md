# CRUD de Productos (Spring Boot + H2)

Proyecto simple: CRUD de productos con buscador que demora 4,5 segundos.

## Requisitos
- Java 17 o superior
- Maven 3.8+ (o abrirlo con IntelliJ / Eclipse / STS)

## Ejecutar
mvn spring-boot:run


## Endpoints (CRUD)
| Método | URL | Acción |
|--------|-----|--------|
| GET | /api/productos | Listar todos |
| GET | /api/productos?q=texto | Buscar por nombre (demora 4,5 s) |
| GET | /api/productos/{id} | Ver un producto |
| POST | /api/productos | Crear |
| PUT | /api/productos/{id} | Actualizar |
| DELETE | /api/productos/{id} | Eliminar |

## Base de datos
H2 (guardada en archivo `./data/productosdb`). No requiere instalar nada.
Consola opcional: http://localhost:8080/h2-console
(JDBC URL: `jdbc:h2:file:./data/productosdb`, usuario `sa`, sin contraseña)

## Cambiar la demora
`DEMORA_BUSQUEDA_MS` en `ProductoController.java` (4500 = 4,5 segundos).

---------------------------------
---------------------------------

Explicación del proyecto crud-productos
Estructura
crud-productos/
├── pom.xml
└── src/main/
    ├── java/com/ejemplo/productos/
    │   ├── CrudProductosApplication.java
    │   ├── Producto.java
    │   ├── ProductoRepository.java
    │   └── ProductoController.java
    └── resources/
        ├── application.properties
        └── static/index.html

Flujo general: el navegador carga index.html → el JavaScript llama a la API (/api/productos) → ProductoController recibe la petición → usa ProductoRepository → este lee y escribe en la tabla producto de H2 (representada por la clase Producto).

1. pom.xml

Define las dependencias de Maven:

spring-boot-starter-web: servidor web y API REST.
spring-boot-starter-data-jpa: acceso a base de datos sin escribir SQL.
h2: la base de datos.

Usa Spring Boot 3.3.5 y Java 17. Si necesitas otra librería, se agrega aquí.

2. CrudProductosApplication.java (clase principal)
@SpringBootApplication y el método main arrancan toda la aplicación.
El método datosIniciales (@Bean CommandLineRunner) se ejecuta al iniciar. Si la tabla está vacía (repo.count() == 0), inserta 6 productos de ejemplo.
Cambios típicos: agregar, quitar o editar los productos de ejemplo.
3. Producto.java (entidad)
@Entity hace que esta clase sea una tabla llamada producto.
Campos (columnas): id (autogenerado con @Id y @GeneratedValue), nombre, descripcion, precio y stock.
Tiene constructor vacío (obligatorio para JPA), constructor con datos, y getters y setters.
Cambios típicos: para agregar una columna nueva (por ejemplo categoria), añade el campo y su getter y setter aquí. Con ddl-auto=update la columna se crea sola. Después debes actualizar el controlador (actualizar) y el index.html.
4. ProductoRepository.java
Es una interfaz que extiende JpaRepository<Producto, Long>. Eso ya da gratis findAll, findById, save, deleteById, existsById y count.
Agrega un método propio: findByNombreContainingIgnoreCase(String nombre). Spring genera la consulta a partir del nombre del método: busca productos cuyo nombre contenga el texto, sin distinguir mayúsculas.
Cambios típicos: para buscar también por descripción, agrega findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase.
4. ProductoController.java (aquí está la API)
@RestController y @RequestMapping("/api/productos"): todas las rutas empiezan con /api/productos.
DEMORA_BUSQUEDA_MS = 4500: la demora de la búsqueda.
Método	Ruta	Qué hace
listar	GET /api/productos	Sin q, devuelve todos. Con ?q=texto, espera 4,5 s con Thread.sleep y filtra por nombre
obtener	GET /api/productos/{id}	Devuelve uno, o 404 si no existe
crear	POST /api/productos	Guarda un producto nuevo (pone id = null para forzar la creación)
actualizar	PUT /api/productos/{id}	Busca el producto, copia los 4 campos nuevos y lo guarda
eliminar	DELETE /api/productos/{id}	Borra el producto, o 404 si no existe
@RequestBody convierte el JSON recibido en un objeto Producto.
Cambios típicos: cambiar la demora (el valor 4500), o quitarla borrando la línea Thread.sleep.
5. application.properties
server.port=8080: puerto de la aplicación.
spring.datasource.url=jdbc:h2:file:./data/productosdb: la base se guarda en un archivo dentro de la carpeta data/ del proyecto.
ddl-auto=update: Hibernate crea o actualiza la tabla automáticamente.
h2.console.enabled=true: habilita la consola en /h2-console.
Cambios típicos: para cambiar de base de datos (MySQL o PostgreSQL), cambia la dependencia en el pom.xml y la URL, el driver, el usuario y la contraseña aquí.
6. static/index.html (la página)

Spring Boot sirve automáticamente todo lo que esté en static/. El archivo tiene HTML, CSS y JavaScript juntos.

HTML:

<header> con la barra de búsqueda (#busqueda) y el botón #btnBuscar. Es la “S” de tu boceto.
#estado: muestra “Buscando...” con spinner o mensajes.
#lista: contenedor de las tarjetas de producto, las “P” del boceto.
#formulario: formulario oculto para crear y editar.

JavaScript (funciones principales):

cargar(q): llama a GET /api/productos. Si hay texto de búsqueda, desactiva el botón y muestra el spinner mientras espera los 4,5 s. Luego llama a pintar.
pintar(productos): genera una tarjeta HTML por cada producto, con botones Editar y Eliminar.
Evento submit del formulario: si hay id oculto hace PUT (editar), y si no hace POST (crear). Después recarga la lista.
editar(id): consulta el producto con GET /{id} y rellena el formulario.
eliminar(id): pide confirmación y hace DELETE.
esc(): escapa el texto para evitar inyección de HTML.

Cambios típicos:

Para mostrar un campo nuevo, edita la plantilla dentro de pintar, el formulario y el objeto producto del submit.
Para cambiar colores o diseño, edita la sección <style>.
Resumen para empezar a hacer cambios
Quiero...	Archivo
Agregar un campo a producto	Producto.java, actualizar en el controlador, index.html
Cambiar la demora de búsqueda	ProductoController.java (DEMORA_BUSQUEDA_MS)
Nueva búsqueda o filtro	ProductoRepository.java y listar en el controlador
Nuevo endpoint	ProductoController.java
Cambiar de base de datos	pom.xml y application.properties
Cambiar el diseño de la página	index.html


html responsive
<meta name="viewport" content="width=device-width, initial-scale=1">

#lista { 
    display: grid; 
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr)); 
    gap: 16px; 
}
¿Qué hace? Utiliza CSS Grid con auto-fill y minmax. Esto le ordena al sistema que calcule cuántas tarjetas caben por renglón de manera automática:

En una computadora o tablet grande, mostrará varias tarjetas en fila.

En un celular pequeño, detecta que no hay espacio para dos columnas de 250px y automáticamente acomoda las tarjetas una debajo de otra en una sola columna.

@media (max-width: 600px) {
    .barra { flex-direction: column; }
    /* ... reglas para apilar formularios y botones ... */
}
¿Qué hace? Funciona como un "interruptor" condicional. El navegador lee esta regla y dice: "Si la pantalla mide 600 pixeles de ancho o menos (es decir, un celular en posición vertical)", aplica los cambios de diseño específicos:

Convierte la barra de búsqueda y los campos del formulario de horizontal a vertical (en columna) para que no se corten los textos.

Cambia el tamaño de los botones del modal para que ocupen todo el ancho y sea muy fácil tocarlos con el dedo.