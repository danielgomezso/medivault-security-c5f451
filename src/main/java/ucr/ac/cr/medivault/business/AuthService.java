package ucr.ac.cr.medivault.business;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ucr.ac.cr.medivault.data.UsuarioRepository;
import ucr.ac.cr.medivault.domain.Usuario;
import ucr.ac.cr.medivault.dto.LoginRequestDto;
import ucr.ac.cr.medivault.dto.LoginResponseDto;
import ucr.ac.cr.medivault.exception.AccessDeniedException;
import ucr.ac.cr.medivault.security.JwtTokenProvider;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UsuarioRepository usuarioRepository, 
                       PasswordEncoder passwordEncoder, 
                       JwtTokenProvider jwtTokenProvider) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponseDto login(LoginRequestDto request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AccessDeniedException("Credenciales inválidas"));

        // Se verifica el hash de la contraseña de manera segura
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            throw new AccessDeniedException("Credenciales inválidas");
        }

        if (!"ACTIVO".equals(usuario.getEstado())) {
            throw new AccessDeniedException("La cuenta se encuentra inactiva o bloqueada");
        }

        // Generar el token con el rol correspondiente
        String token = jwtTokenProvider.generateToken(usuario.getUsername(), usuario.getRol().name());
        
        return new LoginResponseDto(token, usuario.getUsername(), usuario.getRol().name());
    }
}