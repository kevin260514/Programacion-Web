package com.ejemplo.crud.controller;

import com.ejemplo.crud.model.Producto;
import com.ejemplo.crud.repository.ProductoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoRepository repository;

    public ProductoController(ProductoRepository repository) {
        this.repository = repository;
    }

    // GET /api/productos -> listar todos
    @GetMapping
    public List<Producto> listar() {
        return repository.findAll();
    }

    // POST /api/productos -> crear
    @PostMapping
    public Producto crear(@RequestBody Producto producto) {
        producto.setId(null);
        return repository.save(producto);
    }

    // PUT /api/productos/{id} -> actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto datos) {
        return repository.findById(id)
                .map(producto -> {
                    producto.setNombre(datos.getNombre());
                    producto.setTipo(datos.getTipo());
                    producto.setPrecio(datos.getPrecio());
                    producto.setStock(datos.getStock());
                    return ResponseEntity.ok(repository.save(producto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/productos/{id} -> eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
