package src;

import java.util.Scanner; 

public class Main {
        
    public static void main(String[] args) {
        int capacidad = 5;
        GestorTareas gestor = new GestorTareas(capacidad);
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            ejecutarOpcion(opcion, gestor, scanner);
        } while (opcion != 4);

        scanner.close();
        
    }

    public static void mostrarMenu() {
        System.out.println("----- Menú -----");
        System.out.println("1. Agregar tarea");
        System.out.println("2. Mostrar tareas");
        System.out.println("3. Marcar tarea como completada");
        System.out.println("4. Salir");
        System.out.print("Seleccione una opción: ");
    }

    public static void ejecutarOpcion(int opcion, GestorTareas gestor, Scanner scanner) {
        int numTarea;
        

        switch (opcion) {
            case 1:
                System.out.print("\033[H\033[2J"); 
                System.out.print("Ingrese la descripción de la tarea: ");
                String descripcion = scanner.nextLine().trim();
                gestor.agregarTarea(descripcion);
                break;
            case 2:
                System.out.print("\033[H\033[2J");
                System.out.println("Tareas pendientes:");
                gestor.mostrarTareas();
                break;
            case 3:
                System.out.print("\033[H\033[2J");
                System.out.print("Ingrese el número de la tarea a marcar como completada: ");
                numTarea = scanner.nextInt();
                gestor.marcarTareaComoCompletada((numTarea - 1));
                break;
            case 4:
                System.out.println("Saliendo...");
                break;
            default:
                System.out.println("Opción inválida.");
        }
    }

}
