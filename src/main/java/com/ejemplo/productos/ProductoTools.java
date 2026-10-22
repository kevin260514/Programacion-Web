package com.ejemplo.productos;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ProductoTools {

    private final ProductoRepository productoRepository;

    public ProductoTools(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Tool(description = "Obtiene la lista completa de todos los productos y personajes disponibles.")
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @Tool(description = "Busca un producto por su nombre o palabra clave.")
    public List<Producto> buscarProductos(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
}