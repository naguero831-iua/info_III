package integrador.planner;

import java.time.LocalDateTime;

public class Recordatorio implements Comparable<Recordatorio> {
    public String id;
    public LocalDateTime fecha;
    public String dniPaciente;
    public String mensaje;
    public Recordatorio(String id, LocalDateTime fecha, String dni, String msg) {
        this.id = id; this.fecha = fecha; this.dniPaciente = dni; this.mensaje = msg;
    }
    @Override
    public int compareTo(Recordatorio o) { return this.fecha.compareTo(o.fecha); }
    @Override
    public String toString() { return id + "@" + fecha + " -> " + mensaje; }
}
