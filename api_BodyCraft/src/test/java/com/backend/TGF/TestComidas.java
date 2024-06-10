package com.backend.TGF;

import com.backend.TGF.Controller.TFGController;
import com.backend.TGF.dto.ComidaDTO;
import com.backend.TGF.mappers.ComidaMapper;
import com.backend.TGF.model.entity.Comida;
import com.backend.TGF.model.services.Comida.IComidaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TestComidas {

    @InjectMocks
    private TFGController tfgController;

    @Mock
    private IComidaService comidaService;

    @Mock
    private ComidaMapper comidaMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodasLasComidasExito() {
        // Crear una lista de comidas simuladas
        List<Comida> comidas = new ArrayList<>();
        Comida comida1 = new Comida();
        comida1.setTitulo("Comida 1");
        comida1.setKcal(500);
        comida1.setProteinas(30);
        comida1.setCarbohidratos(100);
        comida1.setPic("pic1.jpg");
        comidas.add(comida1);

        Comida comida2 = new Comida();
        comida2.setTitulo("Comida 2");
        comida2.setKcal(600);
        comida2.setProteinas(35);
        comida2.setCarbohidratos(120);
        comida2.setPic("pic2.jpg");
        comidas.add(comida2);

        // Crear una lista de ComidaDTO esperada
        List<ComidaDTO> comidasDTO = comidas.stream()
                .map(comida -> {
                    ComidaDTO dto = new ComidaDTO();
                    dto.setTitulo(comida.getTitulo());
                    dto.setKcal(comida.getKcal());
                    dto.setProteinas(comida.getProteinas());
                    dto.setCarbohidratos(comida.getCarbohidratos());
                    dto.setPic(comida.getPic());
                    return dto;
                })
                .collect(Collectors.toList());

        // Configurar los mocks
        when(comidaService.findAll()).thenReturn(comidas);
        when(comidaMapper.toDTO(any(Comida.class))).thenAnswer(invocation -> {
            Comida comida = invocation.getArgument(0);
            ComidaDTO dto = new ComidaDTO();
            dto.setTitulo(comida.getTitulo());
            dto.setKcal(comida.getKcal());
            dto.setProteinas(comida.getProteinas());
            dto.setCarbohidratos(comida.getCarbohidratos());
            dto.setPic(comida.getPic());
            return dto;
        });

        // Llamar al método del controlador
        ResponseEntity<List<ComidaDTO>> response = tfgController.obtenerTodasLasComidas();

        // Verificar la respuesta
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).getTitulo()).isEqualTo("Comida 1");
        assertThat(response.getBody().get(1).getTitulo()).isEqualTo("Comida 2");

        // Verificar las interacciones con los mocks
        verify(comidaService, times(1)).findAll();
        verify(comidaMapper, times(2)).toDTO(any(Comida.class));
    }

    @Test
    void testObtenerTodasLasComidasListaVacia() {
        // Configurar el servicio para devolver una lista vacía
        when(comidaService.findAll()).thenReturn(new ArrayList<>());

        // Llamar al método del controlador
        ResponseEntity<List<ComidaDTO>> response = tfgController.obtenerTodasLasComidas();

        // Verificar la respuesta
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNullOrEmpty();

        // Verificar las interacciones con los mocks
        verify(comidaService, times(1)).findAll();
        verify(comidaMapper, never()).toDTO(any(Comida.class));
    }
}
