package src;
public class Tarea {

    private String descripcion;
    private boolean estado;

    public Tarea(String descripcion) {
        this.descripcion = descripcion;
        this.estado = false;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void marcarComoCompletada() {
        this.estado = true;
    }
}
