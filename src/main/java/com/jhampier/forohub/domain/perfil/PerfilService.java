package com.jhampier.forohub.domain.perfil;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;

    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    @Transactional
    public DatosRespuestaPerfil registrarPerfil(DatosRegistroPerfil datos) {
        // Verificar si ya existe un perfil con el mismo nombre
        if (perfilRepository.existsByNombre(datos.nombre())) {
            throw new IllegalArgumentException("Ya existe un perfil con este nombre");
        }

        Perfil perfil = new Perfil(null, datos.nombre());
        perfil = perfilRepository.save(perfil);
        return new DatosRespuestaPerfil(perfil);
    }

    public Page<DatosRespuestaPerfil> listarPerfiles(Pageable pageable) {
        return perfilRepository.findAll(pageable)
                .map(DatosRespuestaPerfil::new);
    }

    public DatosRespuestaPerfil obtenerPerfil(Long id) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perfil no encontrado"));
        return new DatosRespuestaPerfil(perfil);
    }

    @Transactional
    public DatosRespuestaPerfil actualizarPerfil(Long id, DatosActualizarPerfil datos) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perfil no encontrado"));

        // Verificar nombre único si se está cambiando
        if (datos.nombre() != null &&
                !datos.nombre().equals(perfil.getNombre()) &&
                perfilRepository.existsByNombre(datos.nombre())) {
            throw new IllegalArgumentException("Ya existe un perfil con este nombre");
        }

        Perfil perfilActualizado = new Perfil(
                perfil.getId(),
                datos.nombre() != null ? datos.nombre() : perfil.getNombre());

        perfilActualizado = perfilRepository.save(perfilActualizado);
        return new DatosRespuestaPerfil(perfilActualizado);
    }

    @Transactional
    public void eliminarPerfil(Long id) {
        if (!perfilRepository.existsById(id)) {
            throw new EntityNotFoundException("Perfil no encontrado");
        }
        perfilRepository.deleteById(id);
    }
}