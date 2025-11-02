package integrador.model;

public class Medico {
    public String matricula;
    public String nombre;
    public String especialidad;
    public Medico(String matricula, String nombre, String esp) {
        this.matricula = matricula; this.nombre = nombre; this.especialidad = esp;
    }
    @Override
    public String toString() { return matricula + " - " + nombre + " (" + especialidad + ")"; }
}
