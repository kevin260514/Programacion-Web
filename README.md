# Tienda Textil - CRUD básico (Spring Boot)

Mini proyecto con Spring Boot + H2 (base de datos en archivo) + una página HTML con JavaScript que consume la API.

## Requisitos

- Java 17 o superior
- Maven (o usar VS Code con las extensiones de abajo)

No hace falta instalar ninguna base de datos: H2 guarda los datos en la carpeta `data/`.

## Abrir y ejecutar en VS Code

1. Instalar las extensiones **Extension Pack for Java** y **Spring Boot Extension Pack**.
2. Abrir la carpeta `tienda-textil` (Archivo > Abrir carpeta).
3. Abrir `CrudApplication.java` y pulsar **Run** (encima del `main`), o desde la terminal:

```bash
mvn spring-boot:run
```

4. Abrir en el navegador: http://localhost:8080

## Endpoints

| Método | URL                   | Descripción  |
|--------|-----------------------|--------------|
| GET    | `/api/productos`      | Listar todos |
| POST   | `/api/productos`      | Crear        |
| PUT    | `/api/productos/{id}` | Actualizar   |
| DELETE | `/api/productos/{id}` | Eliminar     |

Ejemplo de JSON para POST / PUT:

```json
{ "nombre": "Tela de algodón", "tipo": "Tela", "precio": 25.5, "stock": 40 }
```

## Estructura

```
src/main/java/com/ejemplo/crud/
├── CrudApplication.java
├── controller/ProductoController.java
├── model/Producto.java
└── repository/ProductoRepository.java
src/main/resources/
├── application.properties
└── static/
    ├── index.html   (la página)
    └── app.js       (JavaScript: DOM, fetch, async/await, map, find, reduce)
```

## Base de datos

Consola de H2 (opcional): http://localhost:8080/h2-console
JDBC URL: `jdbc:h2:file:./data/tiendatextildb` · Usuario: `sa` · Contraseña: vacía
