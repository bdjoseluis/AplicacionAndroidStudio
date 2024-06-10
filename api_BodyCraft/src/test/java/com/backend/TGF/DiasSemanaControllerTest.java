package com.backend.TGF;

import com.backend.TGF.model.entity.DiasSemana;
import com.backend.TGF.model.entity.Ejercicio;
import com.backend.TGF.model.services.DiasSemana.DiaSemanaService;
import com.backend.TGF.model.services.Ejercicio.EjercicioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@WebMvcTest
public class DiasSemanaControllerTest {
/*
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DiaSemanaService diasemanaService;

    @MockBean
    private EjercicioService ejercicioService;

    @Test
    public void testAddEjercicioToDiaSemana_Success() throws Exception {
        Long diaSemanaId = 2L;
        Long ejercicioId = 1L;
        when(diasemanaService.addEjercicioToDiaSemana(diaSemanaId, ejercicioId)).thenReturn(true);

        mockMvc.perform(post("/diasSemana/{diaSemanaId}/ejercicio/{ejercicioId}", diaSemanaId, ejercicioId))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAddEjercicioToDiaSemana_BadRequest() throws Exception {
        Long diaSemanaId = 2L;
        Long ejercicioId = 1L;

        DiasSemana diaSemana = new DiasSemana();
        Ejercicio ejercicio = new Ejercicio();
        diaSemana.getEjercicios().add(ejercicio);

        when(diasemanaService.findById(diaSemanaId)).thenReturn(diaSemana);
        when(ejercicioService.findById(ejercicioId)).thenReturn(ejercicio);

        mockMvc.perform(post("/diasSemana/{diaSemanaId}/ejercicio/{ejercicioId}", diaSemanaId, ejercicioId))
                .andExpect(status().isBadRequest());
    }*/

    // Agrega más pruebas para otros casos de prueba
}
