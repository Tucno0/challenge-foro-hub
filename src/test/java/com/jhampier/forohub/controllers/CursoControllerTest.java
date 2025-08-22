package com.jhampier.forohub.controllers;

import com.jhampier.forohub.domain.curso.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CursoControllerTest {

    @Mock
    private CursoService cursoService;

    @InjectMocks
    private CursoController cursoController;

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
    @DisplayName("Debería registrar un curso y retornar 201 Created")
    void testRegistrarCurso_DeberiaRetornar201Created() {
        // Given
        when(cursoService.registrarCurso(any(DatosRegistroCurso.class)))
                .thenReturn(datosRespuesta);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

        // When
        ResponseEntity<DatosRespuestaCurso> response = cursoController.registrarCurso(datosRegistro, uriBuilder);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        assertEquals("Java Básico", response.getBody().nombre());
        assertEquals("Programación", response.getBody().categoria());
        verify(cursoService, times(1)).registrarCurso(any(DatosRegistroCurso.class));
    }

    @Test
    @DisplayName("Debería listar cursos paginados y retornar 200 OK")
    void testListarCursos_DeberiaRetornar200OK() {
        // Given
        List<DatosRespuestaCurso> cursos = Arrays.asList(
                new DatosRespuestaCurso(1L, "Java Básico", "Programación"),
                new DatosRespuestaCurso(2L, "Spring Boot", "Framework")
        );
        Page<DatosRespuestaCurso> paginaCursos = new PageImpl<>(cursos, PageRequest.of(0, 10), cursos.size());
        Pageable pageable = PageRequest.of(0, 10);

        when(cursoService.listarCursos(any(Pageable.class))).thenReturn(paginaCursos);

        // When
        ResponseEntity<Page<DatosRespuestaCurso>> response = cursoController.listarCursos(pageable);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
        assertEquals("Java Básico", response.getBody().getContent().get(0).nombre());
        assertEquals("Spring Boot", response.getBody().getContent().get(1).nombre());
        verify(cursoService, times(1)).listarCursos(any(Pageable.class));
    }

    @Test
    @DisplayName("Debería obtener curso por ID y retornar 200 OK")
    void testObtenerCurso_DeberiaRetornar200OK() {
        // Given
        Long cursoId = 1L;
        when(cursoService.obtenerCurso(cursoId)).thenReturn(datosRespuesta);

        // When
        ResponseEntity<DatosRespuestaCurso> response = cursoController.obtenerCurso(cursoId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        assertEquals("Java Básico", response.getBody().nombre());
        assertEquals("Programación", response.getBody().categoria());
        verify(cursoService, times(1)).obtenerCurso(cursoId);
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando curso no existe")
    void testObtenerCurso_DeberiaLanzarExcepcionCuandoNoExiste() {
        // Given
        Long cursoId = 999L;
        when(cursoService.obtenerCurso(cursoId))
                .thenThrow(new EntityNotFoundException("Curso no encontrado"));

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            cursoController.obtenerCurso(cursoId);
        });
        verify(cursoService, times(1)).obtenerCurso(cursoId);
    }

    @Test
    @DisplayName("Debería actualizar curso y retornar 200 OK")
    void testActualizarCurso_DeberiaRetornar200OK() {
        // Given
        Long cursoId = 1L;
        DatosRespuestaCurso cursoActualizado = new DatosRespuestaCurso(1L, "Java Avanzado", "Programación Backend");
        
        when(cursoService.actualizarCurso(eq(cursoId), any(DatosActualizarCurso.class)))
                .thenReturn(cursoActualizado);

        // When
        ResponseEntity<DatosRespuestaCurso> response = cursoController.actualizarCurso(cursoId, datosActualizar);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        assertEquals("Java Avanzado", response.getBody().nombre());
        assertEquals("Programación Backend", response.getBody().categoria());
        verify(cursoService, times(1)).actualizarCurso(eq(cursoId), any(DatosActualizarCurso.class));
    }

    @Test
    @DisplayName("Debería lanzar excepción al actualizar curso inexistente")
    void testActualizarCurso_DeberiaLanzarExcepcionCuandoNoExiste() {
        // Given
        Long cursoId = 999L;
        when(cursoService.actualizarCurso(eq(cursoId), any(DatosActualizarCurso.class)))
                .thenThrow(new EntityNotFoundException("Curso no encontrado"));

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            cursoController.actualizarCurso(cursoId, datosActualizar);
        });
        verify(cursoService, times(1)).actualizarCurso(eq(cursoId), any(DatosActualizarCurso.class));
    }

    @Test
    @DisplayName("Debería eliminar curso y retornar 204 No Content")
    void testEliminarCurso_DeberiaRetornar204NoContent() {
        // Given
        Long cursoId = 1L;
        doNothing().when(cursoService).eliminarCurso(cursoId);

        // When
        ResponseEntity<Void> response = cursoController.eliminarCurso(cursoId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cursoService, times(1)).eliminarCurso(cursoId);
    }

    @Test
    @DisplayName("Debería lanzar excepción al eliminar curso inexistente")
    void testEliminarCurso_DeberiaLanzarExcepcionCuandoNoExiste() {
        // Given
        Long cursoId = 999L;
        doThrow(new EntityNotFoundException("Curso no encontrado"))
                .when(cursoService).eliminarCurso(cursoId);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            cursoController.eliminarCurso(cursoId);
        });
        verify(cursoService, times(1)).eliminarCurso(cursoId);
    }

    @Test
    @DisplayName("Debería actualizar curso con datos parciales")
    void testActualizarCurso_ConDatosParciales() {
        // Given
        Long cursoId = 1L;
        DatosActualizarCurso datosParciales = new DatosActualizarCurso("Java Intermedio", null);
        DatosRespuestaCurso cursoActualizado = new DatosRespuestaCurso(1L, "Java Intermedio", "Programación");
        
        when(cursoService.actualizarCurso(eq(cursoId), any(DatosActualizarCurso.class)))
                .thenReturn(cursoActualizado);

        // When
        ResponseEntity<DatosRespuestaCurso> response = cursoController.actualizarCurso(cursoId, datosParciales);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().id());
        assertEquals("Java Intermedio", response.getBody().nombre());
        assertEquals("Programación", response.getBody().categoria());
        verify(cursoService, times(1)).actualizarCurso(eq(cursoId), any(DatosActualizarCurso.class));
    }

    @Test
    @DisplayName("Debería verificar que el servicio sea llamado con los parámetros correctos")
    void testVerificarLlamadasAlServicio() {
        // Given
        when(cursoService.registrarCurso(any(DatosRegistroCurso.class)))
                .thenReturn(datosRespuesta);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

        // When
        cursoController.registrarCurso(datosRegistro, uriBuilder);

        // Then
        verify(cursoService).registrarCurso(argThat(datos -> 
            "Java Básico".equals(datos.nombre()) && 
            "Programación".equals(datos.categoria())
        ));
    }
}
