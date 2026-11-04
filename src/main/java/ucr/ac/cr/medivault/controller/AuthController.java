package ucr.ac.cr.medivault.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucr.ac.cr.medivault.business.AuthService;
import ucr.ac.cr.medivault.dto.LoginRequestDto;
import ucr.ac.cr.medivault.dto.LoginResponseDto;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        // La validación @Valid ejecuta las reglas definidas en LoginRequestDto
        LoginResponseDto response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}