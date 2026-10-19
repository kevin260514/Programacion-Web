// URL de la API (el backend de Spring Boot)
const API = "/api/productos";

// Lista de productos que viene del backend
let productos = [];

// Elementos del DOM
const formulario = document.getElementById("formulario");
const tabla = document.getElementById("tabla");
const total = document.getElementById("total");
const titulo = document.getElementById("titulo");
const cancelar = document.getElementById("cancelar");

// ---------- READ: pedir los productos al backend ----------
const cargar = async () => {
    const respuesta = await fetch(API);
    productos = await respuesta.json();
    mostrar();
};

// Crea una celda <td> con un texto
const crearCelda = (texto) => {
    const celda = document.createElement("td");
    celda.textContent = texto;
    return celda;
};

// Dibuja la tabla usando el DOM
const mostrar = () => {
    tabla.innerHTML = "";

    productos.forEach((producto) => {
        const { id, nombre, tipo, precio, stock } = producto; // destructuring

        const fila = document.createElement("tr");

        // map: convierte cada dato en una celda
        [id, nombre, tipo, precio, stock]
            .map(crearCelda)
            .forEach((celda) => fila.appendChild(celda));

        // Botones de acciones
        const acciones = document.createElement("td");

        const botonEditar = document.createElement("button");
        botonEditar.textContent = "Editar";
        botonEditar.addEventListener("click", () => editar(id));

        const botonEliminar = document.createElement("button");
        botonEliminar.textContent = "Eliminar";
        botonEliminar.addEventListener("click", () => eliminar(id));

        acciones.appendChild(botonEditar);
        acciones.appendChild(botonEliminar);
        fila.appendChild(acciones);

        tabla.appendChild(fila);
    });

    // reduce: suma precio * stock de todos los productos
    const valorTotal = productos.reduce(
        (suma, { precio, stock }) => suma + precio * stock,
        0
    );
    total.textContent = valorTotal.toFixed(2);
};

// ---------- UPDATE: llenar el formulario con un producto ----------
const editar = (id) => {
    // find: busca el producto con ese id
    const { nombre, tipo, precio, stock } = productos.find((p) => p.id === id);

    document.getElementById("id").value = id;
    document.getElementById("nombre").value = nombre;
    document.getElementById("tipo").value = tipo;
    document.getElementById("precio").value = precio;
    document.getElementById("stock").value = stock;
    titulo.textContent = "Editar producto #" + id;
};

// ---------- DELETE ----------
const eliminar = async (id) => {
    if (!confirm("¿Eliminar este producto?")) return;
    await fetch(`${API}/${id}`, { method: "DELETE" });
    cargar();
};

// Limpia el formulario
const limpiar = () => {
    formulario.reset();
    document.getElementById("id").value = "";
    titulo.textContent = "Nuevo producto";
};

// ---------- CREATE / UPDATE: al enviar el formulario ----------
formulario.addEventListener("submit", async (evento) => {
    evento.preventDefault();

    const id = document.getElementById("id").value;

    const producto = {
        nombre: document.getElementById("nombre").value,
        tipo: document.getElementById("tipo").value,
        precio: parseFloat(document.getElementById("precio").value),
        stock: parseInt(document.getElementById("stock").value),
    };

    // Si hay id es PUT (editar), si no hay es POST (crear)
    await fetch(id ? `${API}/${id}` : API, {
        method: id ? "PUT" : "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(producto),
    });

    limpiar();
    cargar();
});

cancelar.addEventListener("click", limpiar);

// Al abrir la página, cargar los productos
cargar();
