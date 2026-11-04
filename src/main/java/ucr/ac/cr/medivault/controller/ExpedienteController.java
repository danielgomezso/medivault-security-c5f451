package ucr.ac.cr.medivault.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ucr.ac.cr.medivault.business.ExpedienteService;
import ucr.ac.cr.medivault.dto.ExpedienteRequestDto;
import ucr.ac.cr.medivault.dto.ExpedienteResponseDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/expedientes")
public class ExpedienteController {

    private final ExpedienteService expedienteService;

    public ExpedienteController(ExpedienteService expedienteService) {
        this.expedienteService = expedienteService;
    }

    // Endpoint vulnerable a IDOR si no estuviera protegido en el servicio
    @GetMapping("/{id}")
    public ResponseEntity<ExpedienteResponseDto> obtenerExpediente(@PathVariable Long id, Authentication authentication) {
        // Le pasamos el ID solicitado y quién es el usuario que lo solicita
        ExpedienteResponseDto response = expedienteService.obtenerExpedientePorId(id, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mis-expedientes")
    @PreAuthorize("hasAuthority('ROLE_PACIENTE')")
    public ResponseEntity<List<ExpedienteResponseDto>> obtenerMisExpedientes(Authentication authentication) {
        List<ExpedienteResponseDto> response = expedienteService.obtenerMisExpedientes(authentication.getName());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ExpedienteResponseDto> crearExpediente(@Valid @RequestBody ExpedienteRequestDto request, Authentication authentication) {
        ExpedienteResponseDto response = expedienteService.crearExpediente(request, authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}