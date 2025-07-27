package com.jhampier.forohub.domain.usuario;

import com.jhampier.forohub.domain.perfil.Perfil;
import com.jhampier.forohub.domain.perfil.PerfilRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public DatosRespuestaUsuario registrarUsuario(DatosRegistroUsuario datos) {
        // Verificar si el correo ya existe
        if (usuarioRepository.existsByCorreoElectronico(datos.correoElectronico())) {
            throw new IllegalArgumentException("Ya existe un usuario con este correo electrónico");
        }

        // Obtener perfiles
        Set<Perfil> perfiles = obtenerPerfiles(datos.perfilesIds());

        // Crear usuario con contraseña encriptada
        Usuario usuario = new Usuario(
                null,
                datos.nombre(),
                datos.correoElectronico(),
                passwordEncoder.encode(datos.contrasena()),
                perfiles);

        usuario = usuarioRepository.save(usuario);
        return new DatosRespuestaUsuario(usuario);
    }

    public Page<DatosRespuestaUsuario> listarUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(DatosRespuestaUsuario::new);
    }

    public DatosRespuestaUsuario obtenerUsuario(Long id) {
        Usuario usuario = usuarioRepository.findByIdWithPerfiles(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        return new DatosRespuestaUsuario(usuario);
    }

    @Transactional
    public DatosRespuestaUsuario actualizarUsuario(Long id, DatosActualizarUsuario datos) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Verificar correo único si se está cambiando
        if (datos.correoElectronico() != null &&
                !datos.correoElectronico().equals(usuario.getCorreoElectronico()) &&
                usuarioRepository.existsByCorreoElectronico(datos.correoElectronico())) {
            throw new IllegalArgumentException("Ya existe un usuario con este correo electrónico");
        }

        // Actualizar campos si no son nulos
        Usuario usuarioActualizado = new Usuario(
                usuario.getId(),
                datos.nombre() != null ? datos.nombre() : usuario.getNombre(),
                datos.correoElectronico() != null ? datos.correoElectronico() : usuario.getCorreoElectronico(),
                datos.contrasena() != null ? passwordEncoder.encode(datos.contrasena()) : usuario.getContrasena(),
                datos.perfilesIds() != null ? obtenerPerfiles(datos.perfilesIds()) : usuario.getPerfiles());

        usuarioActualizado = usuarioRepository.save(usuarioActualizado);
        return new DatosRespuestaUsuario(usuarioActualizado);
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    private Set<Perfil> obtenerPerfiles(Set<Long> perfilesIds) {
        if (perfilesIds == null || perfilesIds.isEmpty()) {
            return Set.of();
        }

        return perfilesIds.stream()
                .map(id -> perfilRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Perfil no encontrado: " + id)))
                .collect(Collectors.toSet());
    }
}