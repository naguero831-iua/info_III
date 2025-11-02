package integrador.model;

import java.time.LocalDateTime;

public class Turno implements Comparable<Turno> {
    public String id;
    public String dniPaciente;
    public String matriculaMedico;
    public LocalDateTime fechaHora;
    public int duracionMin;
    public String motivo;

    public Turno(String id, String dniPaciente, String matriculaMedico, LocalDateTime fechaHora, int duracionMin, String motivo) {
        this.id = id; this.dniPaciente = dniPaciente; this.matriculaMedico = matriculaMedico;
        this.fechaHora = fechaHora; this.duracionMin = duracionMin; this.motivo = motivo;
    }

    public Turno deepCopy() {
        return new Turno(id, dniPaciente, matriculaMedico, LocalDateTime.from(fechaHora), duracionMin, motivo);
    }

    public LocalDateTime fin() { return fechaHora.plusMinutes(duracionMin); }

    @Override
    public int compareTo(Turno o) {
        int c = this.fechaHora.compareTo(o.fechaHora);
        if (c != 0) return c;
        return this.id.compareTo(o.id);
    }

    @Override
    public String toString() {
        return id + " | " + dniPaciente + " | " + matriculaMedico + " | " + fechaHora + " dur:" + duracionMin;
    }
}
