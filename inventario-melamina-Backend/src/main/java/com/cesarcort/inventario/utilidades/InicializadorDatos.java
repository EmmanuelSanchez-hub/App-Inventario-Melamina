package com.cesarcort.inventario.utilidades;

import com.cesarcort.inventario.entidades.Usuario;
import com.cesarcort.inventario.repositorios.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Componente que se ejecuta al iniciar la aplicación
 * Hashea las contraseñas en texto plano de los usuarios de la BD
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InicializadorDatos implements CommandLineRunner {
    
    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("===========================================");
        log.info("🔐 Verificando passwords de usuarios...");
        
        List<Usuario> usuarios = usuarioRepositorio.findAll();
        int usuariosActualizados = 0;
        
        for (Usuario usuario : usuarios) {
            // Verificar si la contraseña NO está hasheada (BCrypt usa $2a$ o $2b$)
            if (!usuario.getPassword().startsWith("$2a$") && !usuario.getPassword().startsWith("$2b$")) {
                log.info("   Hasheando password para usuario: {}", usuario.getUsername());
                
                String passwordPlain = usuario.getPassword();
                String passwordHasheada = passwordEncoder.encode(passwordPlain);
                usuario.setPassword(passwordHasheada);
                usuarioRepositorio.save(usuario);
                
                usuariosActualizados++;
            }
        }
        
        if (usuariosActualizados > 0) {
            log.info("✅ {} contraseñas hasheadas correctamente", usuariosActualizados);
        } else {
            log.info("✅ Todas las contraseñas ya están hasheadas");
        }
        
        log.info("===========================================");
    }
}