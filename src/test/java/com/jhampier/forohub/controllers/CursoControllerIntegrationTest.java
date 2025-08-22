package com.jhampier.forohub.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhampier.forohub.domain.curso.*;
import com.jhampier.forohub.infra.security.SecurityFilter;
import com.jhampier.forohub.infra.security.TokenService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = CursoController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityFilter.class}
    )
)
@WithMockUser
class CursoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CursoService cursoService;

    @MockitoBean
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                    .anyRequest().permitAll()
                );
            return http.build();
        }
    }

    private DatosRegistroCurso datosRegistro;
    private DatosActualizarCurso datosActualizar;
    private DatosRespuestaCurso datosRespuesta;

    @BeforeEach
    void setUp() {
        datosRegistro = new DatosRegistroCurso("Java Básico", "Programación");
        datosActualizar = new DatosActualizarCurso("Java Avanzado", "Programación Backend");
        datosRespuesta = new DatosRespuestaCurso(1L, "Java Básico", "Programación");
    }

    @Test
    @DisplayName("POST /cursos - Debería registrar un curso y retornar 201 Created")
    void testRegistrarCurso_DeberiaRetornar201Created() throws Exception {
        // Given
        when(cursoService.registrarCurso(any(DatosRegistroCurso.class)))
                .thenReturn(datosRespuesta);

        // When & Then
        mockMvc.perform(post("/cursos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datosRegistro)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Java Básico"))
                .andExpect(jsonPath("$.categoria").value("Programación"));

        verify(cursoService, times(1)).registrarCurso(any(DatosRegistroCurso.class));
    }

    @Test
    @DisplayName("POST /cursos - Debería fallar con datos inválidos y retornar 400 Bad Request")
    void testRegistrarCurso_DeberiaFallarConDatosInvalidos() throws Exception {
        // Given
        DatosRegistroCurso datosInvalidos = new DatosRegistroCurso("", "");

        // When & Then
        mockMvc.perform(post("/cursos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datosInvalidos)))
                .andExpect(status().isBadRequest());

        verify(cursoService, never()).registrarCurso(any(DatosRegistroCurso.class));
    }

    @Test
    @DisplayName("GET /cursos - Debería listar cursos paginados y retornar 200 OK")
    void testListarCursos_DeberiaRetornar200OK() throws Exception {
        // Given
        List<DatosRespuestaCurso> cursos = Arrays.asList(
                new DatosRespuestaCurso(1L, "Java Básico", "Programación"),
                new DatosRespuestaCurso(2L, "Spring Boot", "Framework")
        );
        Page<DatosRespuestaCurso> paginaCursos = new PageImpl<>(cursos, PageRequest.of(0, 10), cursos.size());

        when(cursoService.listarCursos(any(Pageable.class))).thenReturn(paginaCursos);

        // When & Then
        mockMvc.perform(get("/cursos")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].nombre").value("Java Básico"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].nombre").value("Spring Boot"));

        verify(cursoService, times(1)).listarCursos(any(Pageable.class));
    }

    @Test
    @DisplayName("GET /cursos/{id} - Debería obtener curso por ID y retornar 200 OK")
    void testObtenerCurso_DeberiaRetornar200OK() throws Exception {
        // Given
        Long cursoId = 1L;
        when(cursoService.obtenerCurso(cursoId)).thenReturn(datosRespuesta);

        // When & Then
        mockMvc.perform(get("/cursos/{id}", cursoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Java Básico"))
                .andExpect(jsonPath("$.categoria").value("Programación"));

        verify(cursoService, times(1)).obtenerCurso(cursoId);
    }

    @Test
    @DisplayName("GET /cursos/{id} - Debería retornar 404 cuando curso no existe")
    void testObtenerCurso_DeberiaRetornar404CuandoNoExiste() throws Exception {
        // Given
        Long cursoId = 999L;
        when(cursoService.obtenerCurso(cursoId))
                .thenThrow(new EntityNotFoundException("Curso no encontrado"));

        // When & Then
        mockMvc.perform(get("/cursos/{id}", cursoId))
                .andExpect(status().isNotFound());

        verify(cursoService, times(1)).obtenerCurso(cursoId);
    }

    @Test
    @DisplayName("PUT /cursos/{id} - Debería actualizar curso y retornar 200 OK")
    void testActualizarCurso_DeberiaRetornar200OK() throws Exception {
        // Given
        Long cursoId = 1L;
        DatosRespuestaCurso cursoActualizado = new DatosRespuestaCurso(1L, "Java Avanzado", "Programación Backend");
        
        when(cursoService.actualizarCurso(eq(cursoId), any(DatosActualizarCurso.class)))
                .thenReturn(cursoActualizado);

        // When & Then
        mockMvc.perform(put("/cursos/{id}", cursoId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datosActualizar)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Java Avanzado"))
                .andExpect(jsonPath("$.categoria").value("Programación Backend"));

        verify(cursoService, times(1)).actualizarCurso(eq(cursoId), any(DatosActualizarCurso.class));
    }

    @Test
    @DisplayName("DELETE /cursos/{id} - Debería eliminar curso y retornar 204 No Content")
    void testEliminarCurso_DeberiaRetornar204NoContent() throws Exception {
        // Given
        Long cursoId = 1L;
        doNothing().when(cursoService).eliminarCurso(cursoId);

        // When & Then
        mockMvc.perform(delete("/cursos/{id}", cursoId)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(cursoService, times(1)).eliminarCurso(cursoId);
    }

    @Test
    @DisplayName("POST /cursos - Debería validar nombre requerido")
    void testRegistrarCurso_DeberiaValidarNombreRequerido() throws Exception {
        // Given
        DatosRegistroCurso datosInvalidos = new DatosRegistroCurso(null, "Programación");

        // When & Then
        mockMvc.perform(post("/cursos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datosInvalidos)))
                .andExpect(status().isBadRequest());

        verify(cursoService, never()).registrarCurso(any(DatosRegistroCurso.class));
    }
}
