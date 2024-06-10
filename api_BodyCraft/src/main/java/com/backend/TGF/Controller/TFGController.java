package com.backend.TGF.Controller;

import com.backend.TGF.dto.*;
import com.backend.TGF.exceptions.InvalidDataException;
import com.backend.TGF.exceptions.Response;
import com.backend.TGF.mappers.ComidaMapper;
import com.backend.TGF.mappers.DiasSemanaMapper;
import com.backend.TGF.mappers.EjercicioMapper;
import com.backend.TGF.mappers.ProductoMapper;
import com.backend.TGF.model.entity.*;
import com.backend.TGF.model.services.Comida.IComidaService;
import com.backend.TGF.model.services.Comprable.IComprableService;
import com.backend.TGF.model.services.DiasSemana.IDiasemanaService;
import com.backend.TGF.model.services.Ejercicio.IEjercicioService;
import com.backend.TGF.model.services.Producto.IProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class TFGController {

    @Autowired
    IDiasemanaService diasemanaService;

    @Autowired
    IEjercicioService ejercicioService;

    @Autowired
    IComidaService comidaService;

    @Autowired
    IComprableService comprableService;

    @Autowired
    IProductoService productoService;

    @Autowired
    DiasSemanaMapper diasSemanaMapper;

    @Autowired
    EjercicioMapper ejercicioMapper;

    @Autowired
    ComidaMapper comidaMapper;

    @Autowired
    ProductoMapper productoMapper;

    @Operation(summary = "Método que crea un día de la semana")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Día de la semana creado",
                    content = @Content(schema = @Schema(implementation = DiasSemana.class))),
            @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No se pudo crear el día de la semana",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping("/addDiaSemana")
    public ResponseEntity<DiasSemanaDTO> addDiaSemana(@RequestBody CreateDiasSemanaDTO createDiasSemanaDTO) {
        // Convierte el CreateDiasSemanaDTO a DiasSemana usando el mapper
        DiasSemana diasSemana = diasSemanaMapper.fromCreateDiasSemanaDTO(createDiasSemanaDTO);

        // Llama al servicio para crear un nuevo día de la semana
        DiasSemana nuevoDiaSemana = diasemanaService.addDiaSemana(diasSemana);

        // Convierte el DiasSemana creado a DiasSemanaDTO
        DiasSemanaDTO nuevoDiaSemanaDTO = diasSemanaMapper.toDTO(nuevoDiaSemana);

        // Retorna el nuevo Día de la Semana como respuesta
        return new ResponseEntity<>(nuevoDiaSemanaDTO, HttpStatus.CREATED);
    }


    @Operation(summary = "Devuelve todas las comidas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comidas devueltas",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ComidaDTO.class)))),
            @ApiResponse(responseCode = "404", description = "No se encontraron comidas",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/comidas")
    public ResponseEntity<List<ComidaDTO>> obtenerTodasLasComidas() {
        // Obtener la lista de todas las comidas utilizando el servicio comidaService
        List<Comida> comidas = comidaService.findAll();

        // Verifica si se encontraron comidas
        if (comidas == null || comidas.isEmpty()) {
            // Si no se encontraron comidas, retorna un código de respuesta 404 (no encontrado)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Convertir la lista de Comida a una lista de ComidaDTO utilizando el ComidaMapper
        List<ComidaDTO> comidasDTO = comidas.stream()
                .map(comidaMapper::toDTO)
                .collect(Collectors.toList());

        // Retornar la lista de ComidaDTOs y un código de respuesta 200 (OK)
        return new ResponseEntity<>(comidasDTO, HttpStatus.OK);
    }

    @Operation(summary = "Método que devuelve todos los días de la semana")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Días de la semana devueltos",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DiasSemanaDTO.class)))),
            @ApiResponse(responseCode = "404", description = "No se encontraron días de la semana",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/diasSemana")
    public ResponseEntity<List<DiasSemanaDTO>> obtenerTodosLosDiasSemana() {
        List<DiasSemana> diasSemana = diasemanaService.findAll();
        List<DiasSemanaDTO> diasSemanaDTOs = diasSemana.stream()
                .map(diasSemanaMapper::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(diasSemanaDTOs, HttpStatus.OK);
    }


    @Operation(summary = "Método que devuelve ejercicios por nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ejercicios devueltos",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Ejercicio.class)))),
            @ApiResponse(responseCode = "404", description = "No se encontraron ejercicios con el nombre proporcionado",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/ejercicios/{nombre}")
    public ResponseEntity<EjercicioDTO> obtenerEjercicioPorNombre(@PathVariable String nombre) {
        // Busca el ejercicio por su nombre utilizando el servicio ejercicioService
        Ejercicio ejercicio = ejercicioService.findByName(nombre);

        // Verifica si se encontró un ejercicio con el nombre proporcionado
        if (ejercicio == null) {
            // Si no se encontró un ejercicio con ese nombre, retorna un código de respuesta 404 (no encontrado)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Convierte el ejercicio a un EjercicioDTO utilizando EjercicioMapper o creando un constructor en EjercicioDTO
        EjercicioDTO ejercicioDTO = new EjercicioDTO(ejercicio.getId(), ejercicio.getNombre(), ejercicio.getGrupoMuscular(), ejercicio.getUrlVideo());

        // Si se encontró un ejercicio, retorna el EjercicioDTO y un código de respuesta 200 (OK)
        return new ResponseEntity<>(ejercicioDTO, HttpStatus.OK);
    }

    @Operation(summary = "Elimina un ejercicio de un día de la semana")
    @DeleteMapping("/diasSemana/{diaSemanaId}/removeEjercicio/{ejercicioId}")
    public ResponseEntity<Response> removeEjercicioFromDiaSemana(@PathVariable Long diaSemanaId, @PathVariable Long ejercicioId) {
        boolean success = diasemanaService.removeEjercicioFromDiaSemana(diaSemanaId, ejercicioId);

        if (success) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Operation(summary = "Método que devuelve todos los ejercicios existentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ejercicios devueltos",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EjercicioDTO.class)))),
            @ApiResponse(responseCode = "404", description = "No se encontraron ejercicios",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/ejercicios")
    public ResponseEntity<Set<EjercicioDTO>> obtenerTodosLosEjercicios() {
        // Obtiene la lista de todos los ejercicios utilizando el servicio ejercicioService
        List<Ejercicio> ejercicios = ejercicioService.findAll();

        // Verifica si se encontraron ejercicios
        if (ejercicios == null || ejercicios.isEmpty()) {
            // Si no se encontraron ejercicios, retorna un código de respuesta 404 (no encontrado)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Convertir la lista de Ejercicio a una lista de EjercicioDTO
        Set<EjercicioDTO> ejerciciosDTO = ejercicioMapper.toDTOSet(ejercicios);

        // Retorna la lista de EjercicioDTO y un código de respuesta 200 (OK)
        return new ResponseEntity<>(ejerciciosDTO, HttpStatus.OK);
    }

    @Operation(summary = "Devuelve un producto por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto devuelto", content = @Content(schema = @Schema(implementation = ProductosDTO.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/productosporid/{id}")
    public ResponseEntity<ProductosDTO> obtenerProductoPorId(@PathVariable("id") Long id) {
        // Busca el producto con la ID especificada utilizando el servicio productoService
        Producto producto = productoService.findById(id);

        // Verifica si el producto fue encontrado
        if (producto == null) {
            // Si no se encontró el producto, retorna un código de respuesta 404 (no encontrado)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Convierte el producto a un ProductosDTO usando ProductoMapper
        ProductosDTO productoDTO = productoMapper.toDTO(producto);

        // Retorna el productoDTO y un código de respuesta 200 (OK)
        return new ResponseEntity<>(productoDTO, HttpStatus.OK);
    }
    @Operation(summary = "Obtener los ejercicios de un día específico de la semana")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ejercicios encontrados para el día de la semana especificado",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Ejercicio.class)))),
            @ApiResponse(responseCode = "404", description = "No se encontraron ejercicios para el día de la semana especificado",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/diasSemana/{diaSemanaId}/ejercicios")
    public ResponseEntity<Set<EjercicioDTO>> obtenerEjerciciosPorDiaSemana(@PathVariable Long diaSemanaId) {
        // Buscar el día de la semana por ID
        DiasSemana diaSemana = diasemanaService.findById(diaSemanaId);

        // Verifica si se encontró el día de la semana
        if (diaSemana == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Obtener la lista de ejercicios asociados con el día de la semana
        List<Ejercicio> ejerciciosList = diaSemana.getEjercicios();

        // Convertir la lista a un Set de DTOs para eliminar duplicados
        Set<EjercicioDTO> ejerciciosDTO = ejerciciosList.stream()
                .map(ejercicioMapper::toDTO)
                .collect(Collectors.toSet());

        // Retornar la lista de ejercicios sin duplicados
        return new ResponseEntity<>(ejerciciosDTO, HttpStatus.OK);
    }


    // Método para agregar un ejercicio a un día de la semana con validación de duplicados
    @PostMapping("/diasSemana/{diaSemanaId}/ejercicio/{ejercicioId}")
    public ResponseEntity<Response> addEjercicioToDiaSemana(@PathVariable Long diaSemanaId, @PathVariable Long ejercicioId) {
        try {
            // Verificar si el ejercicio ya está presente en el día de la semana
            DiasSemana diaSemana = diasemanaService.findById(diaSemanaId);
            Ejercicio ejercicio = ejercicioService.findById(ejercicioId);

            if (diaSemana.getEjercicios().contains(ejercicio)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            boolean success = diasemanaService.addEjercicioToDiaSemana(diaSemanaId, ejercicioId);

            if (success) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (InvalidDataException e) {
            // Manejo de error de datos inválidos
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Manejo de otros errores inesperados
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "Devuelve las comidas asociadas a un día de la semana")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comidas encontradas para el día de la semana especificado",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Comida.class)))),
            @ApiResponse(responseCode = "404", description = "No se encontraron comidas para el día de la semana especificado",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/diasSemana/{diaSemanaId}/comidas")
    public ResponseEntity<List<ComidaDTO>> obtenerComidasPorDiaSemana(@PathVariable Long diaSemanaId) {
        // Buscar el día de la semana utilizando el servicio diasemanaService
        DiasSemana diaSemana = diasemanaService.findById(diaSemanaId);

        // Verificar si el día de la semana existe
        if (diaSemana == null) {
            // Retornar un código de respuesta 404 (no encontrado) si no se encontró el día de la semana
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Obtener la lista de comidas asociadas con el día de la semana
        List<Comida> comidas = diaSemana.getComidas();

        // Verifica si hay comidas
        if (comidas == null || comidas.isEmpty()) {
            // Retornar un código de respuesta 404 (no encontrado) si no se encontraron comidas
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Convertir la lista de comidas a una lista de ComidaDTOs utilizando el ComidaMapper
        List<ComidaDTO> comidasDTO = comidas.stream()
                .map(comidaMapper::toDTO)
                .collect(Collectors.toList());

        // Retornar la lista de ComidaDTOs y un código de respuesta 200 (OK)
        return new ResponseEntity<>(comidasDTO, HttpStatus.OK);
    }


    @Operation(summary = "Devuelve todos los productos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos devueltos", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Producto.class)))),
            @ApiResponse(responseCode = "404", description = "No se encontraron productos", content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/productos")
    public ResponseEntity<List<ProductosDTO>> obtenerTodosLosProductos() {
        // Obtiene la lista de todos los productos utilizando el servicio productoService
        List<Producto> productos = productoService.findAll();

        // Verifica si se encontraron productos
        if (productos == null || productos.isEmpty()) {
            // Si no se encontraron productos, retorna un código de respuesta 404 (no encontrado)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Convierte la lista de productos a una lista de ProductosDTO usando ProductoMapper
        List<ProductosDTO> productosDTO = productos.stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());

        // Si se encontraron productos, retorna la lista de ProductosDTO y un código de respuesta 200 (OK)
        return new ResponseEntity<>(productosDTO, HttpStatus.OK);
    }

    @GetMapping("/comprables")
    public ResponseEntity<List<Comprable>> obtenerTodosLosComprables() {
        // Obtiene la lista de todos los productos utilizando el servicio productoService
        List<Comprable> comprables = comprableService.findAll();

        if (comprables == null || comprables.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(comprables, HttpStatus.OK);
    }

    @Operation(summary = "Busca Comprables por nombre parcial")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comprables encontrados",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Comprable.class)))),
            @ApiResponse(responseCode = "404", description = "No se encontraron Comprables",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/comprables/search")
    public ResponseEntity<List<Comprable>> buscarComprablesPorNombre(@RequestParam String nombre) {
        // Utiliza el servicio para buscar Comprables por nombre parcial
        List<Comprable> comprables = comprableService.findByNameStartingWith(nombre);

        // Verifica si se encontraron Comprables
        if (comprables == null || comprables.isEmpty()) {
            // Si no se encontraron Comprables, retorna un código de respuesta 404 (no encontrado)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Retorna los Comprables encontrados y un código de respuesta 200 (OK)
        return new ResponseEntity<>(comprables, HttpStatus.OK);
    }

    @Operation(summary = "Busca productos por nombre parcial")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductosDTO.class)))),
            @ApiResponse(responseCode = "404", description = "No se encontraron productos",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @GetMapping("/productos/search")
    public ResponseEntity<List<ProductosDTO>> buscarProductosPorNombre(@RequestParam String nombre) {
        // Utiliza el servicio para buscar productos por nombre parcial
        List<Producto> productos = productoService.findByTituloStartingWith(nombre);

        // Verifica si se encontraron productos
        if (productos == null || productos.isEmpty()) {
            // Si no se encontraron productos, retorna un código de respuesta 404 (no encontrado)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Convierte la lista de productos a una lista de ProductosDTO usando ProductoMapper
        List<ProductosDTO> productosDTO = productos.stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());

        // Retorna la lista de ProductosDTO y un código de respuesta 200 (OK)
        return new ResponseEntity<>(productosDTO, HttpStatus.OK);
    }
    @Operation(summary = "Busca ejercicios por nombre parcial")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ejercicios encontrados",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EjercicioDTO.class)))),
            @ApiResponse(responseCode = "404", description = "No se encontraron ejercicios",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })

    @GetMapping("/ejercicios/search")
    public ResponseEntity<List<EjercicioDTO>> buscarEjerciciosPorNombre(@RequestParam String nombre) {
        // Utiliza el servicio para buscar ejercicios por nombre parcial
        List<Ejercicio> ejercicios = ejercicioService.findByNameContaining(nombre);

        // Verifica si se encontraron ejercicios
        if (ejercicios == null || ejercicios.isEmpty()) {
            // Si no se encontraron ejercicios, retorna un código de respuesta 404 (no encontrado)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Convierte la lista de ejercicios a una lista de EjercicioDTOs utilizando EjercicioMapper
        List<EjercicioDTO> ejerciciosDTO = ejercicios.stream()
                .map(ejercicioMapper::toDTO)
                .collect(Collectors.toList());

        // Retorna la lista de EjercicioDTOs y un código de respuesta 200 (OK)
        return new ResponseEntity<>(ejerciciosDTO, HttpStatus.OK);
    }

    @Operation(summary = "Crea comidas según la lista recibida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comidas creadas con éxito",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Comida.class)))),
            @ApiResponse(responseCode = "404", description = "No se pudo crear las comidas",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @PostMapping("/crearComidas")
    public ResponseEntity<List<Comida>> crearComidas(@RequestBody List<CreateComidasDTO> crearComidasDTOs) {
        // Convertir la lista de CreateComidaDTO a una lista de Comida
        List<Comida> comidas = crearComidasDTOs.stream().map(dto -> {
            Comida comida = new Comida();
            comida.setTitulo(dto.getTitulo());
            comida.setPic(dto.getPic());
            comida.setKcal(dto.getKcal());
            comida.setProteinas(dto.getProteinas());
            comida.setGramos(dto.getGramos());
            comida.setCarbohidratos(dto.getCarbohidratos());

            // Obtener el objeto DiasSemana por su ID
            DiasSemana diaSemana = diasemanaService.findById(dto.getDiaSemanaId());
            comida.setDiaSemana(diaSemana);

            return comida;
        }).toList();

        // Guardar las comidas utilizando el servicio de comida
        List<Comida> comidasGuardadas = comidaService.saveAll(comidas);

        // Retornar la lista de comidas guardadas con un estado HTTP 201 (CREATED)
        return new ResponseEntity<>(comidasGuardadas, HttpStatus.CREATED);
    }


    @Operation(summary = "Elimina una comida según la ID recibida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comida eliminada con éxito",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró la comida para eliminar",
                    content = @Content(schema = @Schema(implementation = Response.class)))
    })
    @DeleteMapping("/borrarcomidas/{comidaId}")
    public ResponseEntity<Response> eliminarComida(@PathVariable Long comidaId) {
        // Verifica si la comida con la ID dada existe
        if (comidaService.existsById(comidaId)) {
            comidaService.deleteById(comidaId);
            return new ResponseEntity<>( HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/comidas/{comidaId}/productos")
    public ResponseEntity<List<ProductosDTO>> obtenerProductosPorComida(@PathVariable Long comidaId) {
        // Buscar la comida por su ID utilizando el servicio comidaService
        Comida comida = comidaService.findById(comidaId);

        // Verifica si la comida existe
        if (comida == null) {
            // Retornar un código de respuesta 404 (no encontrado) si no se encontró la comida
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Obtener la lista de productos asociados con la comida
        List<Producto> productos = comida.getProductos();


        for (Producto producto : productos) {
            // Verifica si la comida ya está asociada con el producto
            if (!producto.getComidas().contains(comida)) {
                producto.getComidas().add(comida);
            }
        }
        // Convertir la lista de productos a una lista de ProductoDTOs utilizando ProductoMapper
        List<ProductosDTO> productosDTO = productos.stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());

        // Retornar la lista de ProductoDTOs y un código de respuesta 200 (OK)
        return new ResponseEntity<>(productosDTO, HttpStatus.OK);
    }


    @Operation(summary = "Desasociar un ejercicio de un día de la semana")
    @DeleteMapping("/diasSemana/{diaSemanaId}/desasociarEjercicio/{ejercicioId}")
    public ResponseEntity<Response> desasociarEjercicioDeDiaSemanadesasociarEjercicioDeDiaSemana(@PathVariable Long diaSemanaId, @PathVariable Long ejercicioId) {
        // Obtener el día de la semana por su ID utilizando el servicio diasemanaService
        DiasSemana diaSemana = diasemanaService.findById(diaSemanaId);

        // Verificar si el día de la semana existe
        if (diaSemana == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Obtener el ejercicio por su ID utilizando el servicio ejercicioService
        Ejercicio ejercicio = ejercicioService.findById(ejercicioId);

        // Verificar si el ejercicio existe
        if (ejercicio == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Remover el ejercicio de la lista de ejercicios asociados con el día de la semana
        boolean removed = diaSemana.getEjercicios().remove(ejercicio);

        boolean removed2 = ejercicio.getDiasSemana().remove(diaSemana);
        // Verificar si el ejercicio fue removido con éxito
        if (!removed && removed2) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Guardar los cambios en el servicio diasemanaService
        diasemanaService.save(diaSemana);

        // Retornar una respuesta exitosa
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Asocia un producto a una comida")
    @PostMapping("/comidas/{comidaId}/productos/{productoId}")
    public ResponseEntity<Response> asociarProductoAComida(@PathVariable Long comidaId, @PathVariable Long productoId) {
        // Obtener la comida y el producto por sus IDs utilizando sus respectivos servicios
        Comida comida = comidaService.findById(comidaId);
        Producto producto = productoService.findById(productoId);

        // Verificar si la comida y el producto existen
        if (comida == null || producto == null) {
            // Retornar un código de respuesta 404 si no se encontró la comida o el producto
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Verificar si el producto ya está asociado a la comida
        if (comida.getProductos().contains(producto)) {
            // Retornar un código de respuesta 400 si el producto ya está asociado a la comida
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Asociar el producto a la comida
        comida.getProductos().add(producto);

        // Asociar la comida al producto, si es necesario
        producto.getComidas().add(comida);

        // Guardar los cambios en el servicio comidaService y productoService
        comidaService.save(comida);
        productoService.save(producto);

        // Retornar una respuesta exitosa con un código de respuesta 201
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @Operation(summary = "Desasocia un producto de una comida")
    @DeleteMapping("/comidas/{comidaId}/productos/{productoId}")
    public ResponseEntity<Response> desasociarProductoDeComida(@PathVariable Long comidaId, @PathVariable Long productoId) {
        // Obtener la comida y el producto por sus IDs utilizando sus respectivos servicios
        Comida comida = comidaService.findById(comidaId);
        Producto producto = productoService.findById(productoId);

        // Verificar si la comida y el producto existen
        if (comida == null || producto == null) {
            // Retornar un código de respuesta 404 si no se encontró la comida o el producto
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Verificar si el producto está asociado a la comida
        if (!comida.getProductos().contains(producto)) {
            // Retornar un código de respuesta 400 si el producto no está asociado a la comida
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Desasociar el producto de la comida
        comida.getProductos().remove(producto);

        // Desasociar la comida del producto, si es necesario
        producto.getComidas().remove(comida);

        // Guardar los cambios en el servicio comidaService y productoService
        comidaService.save(comida);
        productoService.save(producto);

        // Retornar una respuesta exitosa con un código de respuesta 200 (OK)
        return new ResponseEntity<>(HttpStatus.OK);
    }





    @Operation(summary = "Crea un nuevo producto")
    @PostMapping("/productocrear")
    public ResponseEntity<ProductosDTO> crearProducto(@RequestBody CreateProductoDTO createProductoDTO) {
        // Convierte el DTO a Producto utilizando el mapper
        Producto nuevoProducto = productoMapper.toEntity(createProductoDTO);

        // Guarda el nuevo producto utilizando el servicio productoService
        Producto productoGuardado = productoService.saveProducto(nuevoProducto);

        // Convierte el producto guardado a DTO para la respuesta
        ProductosDTO productoDTO = productoMapper.toDTO(productoGuardado);

        // Retorna el DTO del producto guardado y un código de respuesta 201 (Creado)
        return new ResponseEntity<>(productoDTO, HttpStatus.CREATED);
    }


    @Operation(summary = "Elimina un producto por su ID")
    @DeleteMapping("/productodelete/{productoId}")
    public ResponseEntity<Response> eliminarProducto(@PathVariable Long productoId) {
        // Verificar si el producto existe
        Producto producto = productoService.findById(productoId);
        if (producto == null) {
            // Retornar un código de respuesta 404 si no se encontró el producto
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Eliminar el producto utilizando el servicio productoService
        productoService.deleteById(productoId);

        // Retornar una respuesta exitosa con un código de respuesta 200 (OK)
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/productos/{marca}")
    public ResponseEntity<List<ProductosDTO>> obtenerProductosPorMarca(@PathVariable String marca) {
        // Buscar los productos por el nombre de la marca
        List<Producto> productos = productoService.obtenerProductosPorMarca(marca);

        // Verificar si se encontraron productos para la marca especificada
        if (productos == null || productos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Utilizar ProductoMapper para convertir la lista de objetos Producto a ProductosDTO
        List<ProductosDTO> productosDTOList = productos.stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());

        // Retornar la lista de ProductosDTO
        return ResponseEntity.ok(productosDTOList);
    }

    @GetMapping("/ejerciciosmusculo/{grupoMuscular}")
    public ResponseEntity<Set<EjercicioDTO>> obtenerEjerciciosPorGrupoMuscular(@PathVariable String grupoMuscular) {
        List<Ejercicio> ejercicios;

        // Si se especifica un grupo muscular, filtra los ejercicios por el grupo
        if (grupoMuscular != null && !grupoMuscular.isEmpty()) {
            ejercicios = ejercicioService.findByGrupoMuscular(grupoMuscular);
        } else {
            // Si no se especifica ningún grupo muscular, devuelve todos los ejercicios
            ejercicios = ejercicioService.findAll();
        }

        // Verifica si se encontraron ejercicios
        if (ejercicios == null || ejercicios.isEmpty()) {
            // Si no se encontraron ejercicios, retorna un código de respuesta 404 (no encontrado)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Convertir la lista de Ejercicio a una lista de EjercicioDTO
        Set<EjercicioDTO> ejerciciosDTO = ejercicioMapper.toDTOSet(ejercicios);

        // Retorna la lista de EjercicioDTO y un código de respuesta 200 (OK)
        return new ResponseEntity<>(ejerciciosDTO, HttpStatus.OK);
    }
    @Operation(summary = "Actualiza una comida existente")
    @PutMapping("/comidaUpdate/{id}")
    public ResponseEntity<ComidaDTO> actualizarComida(@PathVariable Long id, @RequestBody ActualizarComidaDTO actualizarComidaDTO) {
        // Encuentra la entidad Comida por su ID
        Comida comida = comidaService.findById(id);
        if (comida == null) {
            return ResponseEntity.notFound().build();
        }

        // Actualiza la entidad Comida utilizando ComidaMapper y los datos recibidos
        comidaMapper.updateEntityFromDTO(comida, actualizarComidaDTO);

        // Guarda la entidad actualizada
        comidaService.save(comida);

        // Convierte la entidad actualizada a ComidaDTO para responder al cliente
        ComidaDTO comidaDTO = comidaMapper.toDTO(comida);

        // Responde con la entidad actualizada en formato DTO
        return ResponseEntity.ok(comidaDTO);
    }
}

