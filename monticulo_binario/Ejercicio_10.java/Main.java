package monticulo_binario.integrador;

import java.util.Scanner;
import monticulo_binario.MinHeap;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        MinHeap<Tarea> heapTareas = new MinHeap<>(10);

        System.out.println("=== Gestor de Tareas con Montículo Mínimo ===");

        do {
            // Mostrar menú
            System.out.println("\nSeleccione una opción:");
            System.out.println("1. Agregar tareas");
            System.out.println("2. Ver la próxima tarea urgente (peek)");
            System.out.println("3. Completar la tarea más urgente (poll)");
            System.out.println("4. Mostrar todas las tareas pendientes");
            System.out.println("5. Salir");
            System.out.print("Opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir salto de línea

            switch (opcion) {

                case 1:
                    // Agregar tareas
                    char op;
                    do {
                        System.out.print("Nombre de la tarea: ");
                        String nombre = scanner.nextLine();

                        System.out.print("Prioridad (número entero, menor es más urgente): ");
                        int prioridad = scanner.nextInt();
                        scanner.nextLine(); // Consumir salto de línea

                        Tarea nuevaTarea = new Tarea(nombre, prioridad);
                        heapTareas.add(nuevaTarea);
                        System.out.println("Tarea agregada: " + nuevaTarea);

                        System.out.print("¿Desea agregar otra tarea? (s/n): ");
                        op = scanner.nextLine().charAt(0);

                    } while (op != 'n' && op != 'N');
                    break;

                case 2:
                    // Ver la próxima tarea urgente (peek)
                    try {
                        Tarea tareaProxima = heapTareas.peek(); // requiere método peek() en MinHeap
                        System.out.println("Próxima tarea urgente: " + tareaProxima);
                    } catch (Exception e) {
                        System.out.println("No hay tareas para atender.");
                    }
                    break;

                case 3:
                    // Completar la tarea más urgente (poll)
                    try {
                        Tarea tareaCompletada = heapTareas.poll();
                        System.out.println("Tarea completada: " + tareaCompletada);
                    } catch (Exception e) {
                        System.out.println("No hay tareas para completar.");
                    }
                    break;

                case 4:
                    // Mostrar todas las tareas pendientes en orden de prioridad
                    if (heapTareas.isEmpty()) {
                        System.out.println("No hay tareas pendientes.");
                    } else {
                        System.out.println("Tareas pendientes en orden de prioridad:");
                        // Creamos una copia del heap original
                        MinHeap<Tarea> copiaHeap = new MinHeap<>(10);

                        // Extraemos todas las tareas con poll y las agregamos a la copia
                        while (!heapTareas.isEmpty()) {
                            Tarea t = heapTareas.poll();
                            System.out.println(t);
                            copiaHeap.add(t); // agregamos a la copia para no perderlas
                        }

                        // Restauramos el heap original
                        while (!copiaHeap.isEmpty()) {
                            heapTareas.add(copiaHeap.poll());
                        }
                    }
                    break;

                case 5:
                    System.out.println("Saliendo del gestor de tareas.");
                    break;

                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }

        } while (opcion != 5);

        scanner.close();
    }
}
