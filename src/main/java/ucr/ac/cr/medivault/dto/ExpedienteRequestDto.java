package ucr.ac.cr.medivault.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ExpedienteRequestDto {

    @NotNull(message = "El ID del paciente es requerido")
    private Long pacienteId;

    @NotBlank(message = "El diagnóstico principal no puede estar vacío")
    @Size(max = 255, message = "El diagnóstico no debe exceder los 255 caracteres")
    private String diagnosticoPrincipal;

    @NotBlank(message = "Las notas clínicas no pueden estar vacías")
    private String notasClinicas;

    @NotBlank(message = "El nivel de confidencialidad es requerido")
    private String nivelConfidencialidad;

    public ExpedienteRequestDto() {}

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public String getDiagnosticoPrincipal() { return diagnosticoPrincipal; }
    public void setDiagnosticoPrincipal(String d) { this.diagnosticoPrincipal = d; }

    public String getNotasClinicas() { return notasClinicas; }
    public void setNotasClinicas(String n) { this.notasClinicas = n; }

    public String getNivelConfidencialidad() { return nivelConfidencialidad; }
    public void setNivelConfidencialidad(String n) { this.nivelConfidencialidad = n; }
}