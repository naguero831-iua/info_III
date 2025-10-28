package ejercicio_10;

public class Main {
    public static void main(String[] args) {
        ListaEnlazadaAlumnos lista = new ListaEnlazadaAlumnos();

        // Agregar tres alumnos
        lista.agregarAlumno("Juan Perez", 1001);
        lista.agregarAlumno("Maria Lopez", 1002);
        lista.agregarAlumno("Carlos Gomez", 1003);

        // Imprimir la lista de alumnos
        System.out.println("Lista de alumnos:");
        lista.imprimirLista();

        // Buscar un alumno
        System.out.println("\nBuscando al alumno con legajo 1002:");
        String alumno = lista.buscarAlumnoDetalle(1002);
        if (alumno != null) {
            System.out.println("Alumno encontrado: " + alumno);
        } else {
            System.out.println("Alumno no encontrado");
        }

        // Eliminar un alumno
        System.out.println("\nEliminando al alumno con legajo 1001.");
        lista.eliminarAlumno(1001);

        // Imprimir la lista actualizada
        System.out.println("\nLista de alumnos actualizada:");
        lista.imprimirLista();
    }
}
