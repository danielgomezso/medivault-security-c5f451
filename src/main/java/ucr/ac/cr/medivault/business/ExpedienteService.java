package ucr.ac.cr.medivault.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucr.ac.cr.medivault.data.ExpedienteMedicoRepository;
import ucr.ac.cr.medivault.data.UsuarioRepository;
import ucr.ac.cr.medivault.domain.ExpedienteMedico;
import ucr.ac.cr.medivault.domain.Rol;
import ucr.ac.cr.medivault.domain.Usuario;
import ucr.ac.cr.medivault.dto.ExpedienteRequestDto;
import ucr.ac.cr.medivault.dto.ExpedienteResponseDto;
import ucr.ac.cr.medivault.exception.AccessDeniedException;
import ucr.ac.cr.medivault.exception.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExpedienteService {

    private final ExpedienteMedicoRepository expedienteRepository;
    private final UsuarioRepository usuarioRepository;

    public ExpedienteService(ExpedienteMedicoRepository expedienteRepository, UsuarioRepository usuarioRepository) {
        this.expedienteRepository = expedienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public ExpedienteResponseDto obtenerExpedientePorId(Long id, String usernameAutenticado) {
        ExpedienteMedico expediente = expedienteRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("El expediente solicitado no existe."));

        Usuario usuarioActual = usuarioRepository.findByUsername(usernameAutenticado)
                .orElseThrow(() -> new AccessDeniedException("Usuario no autenticado."));

        // =================================================================
        // MITIGACIÓN IDOR (BOLA): Control de Acceso a Nivel de Objeto
        // =================================================================
        boolean esPropietario = expediente.getPaciente().getId().equals(usuarioActual.getId());
        boolean esMedicoAsignado = expediente.getMedicoAsignado().getId().equals(usuarioActual.getId());
        boolean esAdmin = usuarioActual.getRol() == Rol.ROLE_ADMIN;

        // Si el usuario no cumple ninguna de las condiciones de propiedad, bloqueamos el acceso
        if (!esPropietario && !esMedicoAsignado && !esAdmin) {
            throw new AccessDeniedException("Alerta de Seguridad: No tiene permisos para acceder a este expediente médico confidencial.");
        }

        return new ExpedienteResponseDto(expediente);
    }

    @Transactional(readOnly = true)
    public List<ExpedienteResponseDto> obtenerMisExpedientes(String usernameAutenticado) {
        Usuario paciente = usuarioRepository.findByUsername(usernameAutenticado)
                .orElseThrow(() -> new AccessDeniedException("Usuario no autenticado."));

        return expedienteRepository.findByPacienteId(paciente.getId())
                .stream()
                .map(ExpedienteResponseDto::new)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ExpedienteResponseDto crearExpediente(ExpedienteRequestDto request, String usernameMedico) {
         Usuario medico = usuarioRepository.findByUsername(usernameMedico)
                .orElseThrow(() -> new AccessDeniedException("Médico no autenticado."));
                
         Usuario paciente = usuarioRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("El paciente indicado no existe."));
                
         ExpedienteMedico nuevoExpediente = new ExpedienteMedico();
         nuevoExpediente.setPaciente(paciente);
         nuevoExpediente.setMedicoAsignado(medico);
         // Genera un código de expediente único
         nuevoExpediente.setCodigoExpediente("EXP-2026-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase());
         nuevoExpediente.setDiagnosticoPrincipal(request.getDiagnosticoPrincipal());
         nuevoExpediente.setNotasClinicas(request.getNotasClinicas());
         nuevoExpediente.setNivelConfidencialidad(request.getNivelConfidencialidad());
         
         ExpedienteMedico guardado = expedienteRepository.save(nuevoExpediente);
         return new ExpedienteResponseDto(guardado);
    }
}