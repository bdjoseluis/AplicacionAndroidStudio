package com.backend.TGF;

import com.backend.TGF.Controller.TFGController;
import com.backend.TGF.dto.CreateDiasSemanaDTO;
import com.backend.TGF.dto.DiasSemanaDTO;
import com.backend.TGF.mappers.DiasSemanaMapper;
import com.backend.TGF.model.entity.DiasSemana;
import com.backend.TGF.model.entity.User;
import com.backend.TGF.model.services.DiasSemana.IDiasemanaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@SpringBootTest
public class TestDiasSemana {

    @InjectMocks
    private TFGController tfgController;

    @Mock
    private IDiasemanaService diasemanaService;

    @Mock
    private DiasSemanaMapper diasSemanaMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void testAddDiaSemanaSuccess() {
        // Configura los datos de entrada
        CreateDiasSemanaDTO createDiasSemanaDTO = new CreateDiasSemanaDTO();
        createDiasSemanaDTO.setTitulo("Día de la semana 1");
        createDiasSemanaDTO.setPic("imagen.jpg");
        createDiasSemanaDTO.setUsuarioId(1L);
        createDiasSemanaDTO.setEjerciciosIds(Collections.emptyList());
        createDiasSemanaDTO.setComidasIds(Collections.emptyList());

        // Crea un objeto DiasSemana esperado
        DiasSemana diasSemana = new DiasSemana();
        diasSemana.setTitulo("Día de la semana 1");
        diasSemana.setPic("imagen.jpg");
        User user = new User();
        user.setId(1L);
        diasSemana.setUser(user);

        // Configura los mapeos
        when(diasSemanaMapper.fromCreateDiasSemanaDTO(createDiasSemanaDTO)).thenReturn(diasSemana);

        // Crea un objeto DiasSemanaDTO esperado
        DiasSemanaDTO diasSemanaDTO = new DiasSemanaDTO();
        diasSemanaDTO.setTitulo("Día de la semana 1");
        diasSemanaDTO.setPic("imagen.jpg");
        diasSemanaDTO.setUsuarioId(1L);

        // Configura la respuesta del servicio
        when(diasemanaService.addDiaSemana(diasSemana)).thenReturn(diasSemana);

        // Configura el mapeo de retorno
        when(diasSemanaMapper.toDTO(diasSemana)).thenReturn(diasSemanaDTO);

        // Realiza la operación de agregar un nuevo día de la semana
        ResponseEntity<DiasSemanaDTO> response = tfgController.addDiaSemana(createDiasSemanaDTO);

        // Verifica los resultados
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(response.getBody().getTitulo()).isEqualTo("Día de la semana 1");
        assertThat(response.getBody().getPic()).isEqualTo("imagen.jpg");
        assertThat(response.getBody().getUsuarioId()).isEqualTo(1L);

        // Verifica las interacciones con los mocks
        verify(diasSemanaMapper, times(1)).fromCreateDiasSemanaDTO(createDiasSemanaDTO);
        verify(diasemanaService, times(1)).addDiaSemana(diasSemana);
        verify(diasSemanaMapper, times(1)).toDTO(diasSemana);
    }

   /* @Test
    public void testAddDiaSemanaInvalidData() {
        // Configura los datos de entrada inválidos
        CreateDiasSemanaDTO createDiasSemanaDTO = new CreateDiasSemanaDTO();
        createDiasSemanaDTO.setTitulo(""); // Título vacío, lo cual es inválido

        // Realiza la operación de agregar un nuevo día de la semana
        ResponseEntity<DiasSemanaDTO> response = tfgController.addDiaSemana(createDiasSemanaDTO);

        // Verifica que se devuelva un código de estado de solicitud inválida
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isNull();

        // Verifica que el servicio no se haya llamado, ya que los datos de entrada eran inválidos
        verify(diasemanaService, never()).addDiaSemana(any());
    }
*/
    @Test
    void testObtenerTodosLosDiasSemanaExito() {
        // Crear una lista de DiasSemana simulada
        List<DiasSemana> diasSemanaList = new ArrayList<>();
        DiasSemana dia1 = new DiasSemana();
        dia1.setTitulo("Día 1");
        diasSemanaList.add(dia1);

        DiasSemana dia2 = new DiasSemana();
        dia2.setTitulo("Día 2");
        diasSemanaList.add(dia2);

        // Crear una lista de DiasSemanaDTO esperada
        List<DiasSemanaDTO> diasSemanaDTOList = diasSemanaList.stream()
                .map(diasSemana -> {
                    DiasSemanaDTO dto = new DiasSemanaDTO();
                    dto.setTitulo(diasSemana.getTitulo());
                    return dto;
                })
                .collect(Collectors.toList());

        // Configurar los mocks
        when(diasemanaService.findAll()).thenReturn(diasSemanaList);
        when(diasSemanaMapper.toDTO(any(DiasSemana.class))).thenAnswer(invocation -> {
            DiasSemana diasSemana = invocation.getArgument(0);
            DiasSemanaDTO dto = new DiasSemanaDTO();
            dto.setTitulo(diasSemana.getTitulo());
            return dto;
        });

        // Llamar al método del controlador
        ResponseEntity<List<DiasSemanaDTO>> response = tfgController.obtenerTodosLosDiasSemana();

        // Verificar la respuesta
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);

        // Verificar las interacciones con los mocks
        verify(diasemanaService, times(1)).findAll();
        verify(diasSemanaMapper, times(2)).toDTO(any(DiasSemana.class));
    }

    @Test
    void testObtenerTodosLosDiasSemanaListaVacia() {
        // Configurar el servicio para devolver una lista vacía
        when(diasemanaService.findAll()).thenReturn(new ArrayList<>());

        // Llamar al método del controlador
        ResponseEntity<List<DiasSemanaDTO>> response = tfgController.obtenerTodosLosDiasSemana();

        // Verificar la respuesta
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();

        // Verificar las interacciones con los mocks
        verify(diasemanaService, times(1)).findAll();
        verify(diasSemanaMapper, never()).toDTO(any(DiasSemana.class));
    }
}
