package integrador.main;

import integrador.ejercicios.EjerciciosIntegrador;
import integrador.csv.CSVLoader;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        EjerciciosIntegrador ej = new EjerciciosIntegrador();
        CSVLoader loader = new CSVLoader();

        System.out.println("=== PRÁCTICO INTEGRADOR - Sistema de Turnos ===");
        while (true) {
            System.out.println("\nMenú:");
            System.out.println("0. Cargar CSVs de ejemplo (data/)");
            System.out.println("1. Ejercicio 1 - Validaciones / resumen");
            System.out.println("2. Ejercicio 2 - Agenda por médico (agendar/cancelar/siguiente)");
            System.out.println("3. Ejercicio 3 - Buscar primer hueco libre");
            System.out.println("4. Ejercicio 4 - Sala de espera (cola circular)");
            System.out.println("5. Ejercicio 5 - Planner (recordatorios, min-heap)");
            System.out.println("6. Ejercicio 6 - Índice pacientes (hash chaining propio)");
            System.out.println("7. Ejercicio 7 - Merge/dedup demo");
            System.out.println("8. Ejercicio 8 - Ordenamientos (Shell, Quick)");
            System.out.println("9. Ejercicio 9 - Auditoría / Undo-Redo");
            System.out.println("10. Ejercicio 10 - Planificador de quirófano (heap + top-K)");
            System.out.println("99. Ejecutar todo demo");
            System.out.println("q. Salir");
            System.out.print("Opción: ");
            String op = sc.nextLine().trim();
            if (op.equalsIgnoreCase("q")) break;
            try {
                int o = Integer.parseInt(op);
                switch (o) {
                    case 0 -> {
                        loader.loadAll("integrador/data/pacientes.csv", "integrador/data/medicos.csv", "integrador/data/turnos.csv", ej);
                    }
                    case 1 -> ej.ej1_validaciones();
                    case 2 -> ej.ej2_agenda_demo();
                    case 3 -> ej.ej3_primerHueco_demo();
                    case 4 -> ej.ej4_salaEspera_demo();
                    case 5 -> ej.ej5_planner_demo();
                    case 6 -> ej.ej6_hashpacientes_demo();
                    case 7 -> ej.ej7_merge_demo();
                    case 8 -> ej.ej8_sorts_demo();
                    case 9 -> ej.ej9_audit_demo();
                    case 10 -> ej.ej10_quirofano_demo();
                    case 99 -> ej.runAllDemos();
                    default -> System.out.println("Opción inválida");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Comando no reconocido.");
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                ex.printStackTrace(System.out);
            }
        }
        sc.close();
        System.out.println("Fin.");
    }
}
