package monticulo_binario.paciente;

public class Paciente implements Comparable<Paciente> {
    //comparable es para que el montículo pueda ordenar los pacientes según su prioridad
    String nombre;
    int prioridad; // 1 = alta, 2 = media, 3 = baja

    public Paciente(String nombre, int prioridad) {
        this.nombre = nombre;
        this.prioridad = prioridad;
    }

    // El heap usa este método para comparar pacientes
    @Override
    public int compareTo(Paciente otro) {
        // Prioridad 1 (alta) debe ir antes que 2 o 3, así que se ordena por número ascendente
        return Integer.compare(this.prioridad, otro.prioridad);
    }

    @Override
    public String toString() {
        String nivel = switch (prioridad) {
            case 1 -> "Alta";
            case 2 -> "Media";
            case 3 -> "Baja";
            default -> "Desconocida";
        };
        return nombre + " (Prioridad: " + nivel + ")";
    }
}
