package ucr.ac.cr.medivault.dto;

import ucr.ac.cr.medivault.domain.ExpedienteMedico;
import java.time.LocalDateTime;

public class ExpedienteResponseDto {
    private String codigoExpediente;
    private String diagnosticoPrincipal;
    private String notasClinicas;
    private String nivelConfidencialidad;
    private LocalDateTime fechaRegistro;
    private String nombrePaciente;
    private String nombreMedico;

    public ExpedienteResponseDto(ExpedienteMedico expediente) {
        this.codigoExpediente = expediente.getCodigoExpediente();
        this.diagnosticoPrincipal = expediente.getDiagnosticoPrincipal();
        this.notasClinicas = expediente.getNotasClinicas();
        this.nivelConfidencialidad = expediente.getNivelConfidencialidad();
        this.fechaRegistro = expediente.getFechaRegistro();
        this.nombrePaciente = expediente.getPaciente().getNombreCompleto();
        this.nombreMedico = expediente.getMedicoAsignado().getNombreCompleto();
    }

    // Getters
    public String getCodigoExpediente() { return codigoExpediente; }
    public String getDiagnosticoPrincipal() { return diagnosticoPrincipal; }
    public String getNotasClinicas() { return notasClinicas; }
    public String getNivelConfidencialidad() { return nivelConfidencialidad; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public String getNombrePaciente() { return nombrePaciente; }
    public String getNombreMedico() { return nombreMedico; }
}