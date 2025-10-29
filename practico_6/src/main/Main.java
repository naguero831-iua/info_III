package main;

import java.util.Scanner;
import rbt.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int op;

        do {
            System.out.println("\n======= MENÚ TP ÁRBOL ROJINEGRO =======");
            System.out.println("1. Nodo y NIL sentinel");
            System.out.println("2. Rotaciones izquierda/derecha");
            System.out.println("3. Inserción BST sin balance");
            System.out.println("4. Clasificador de caso");
            System.out.println("5. FixInsert (recoloreo y rotaciones)");
            System.out.println("6. Successor y Predecessor");
            System.out.println("7. Consulta por rango");
            System.out.println("8. Verificadores de invariantes");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            op = sc.nextInt();

            switch (op) {
                case 1 -> EjerciciosRBT.ejercicio1();
                case 2 -> EjerciciosRBT.ejercicio2y3();
                case 3 -> EjerciciosRBT.ejercicio4();
                case 4 -> EjerciciosRBT.ejercicio5();
                case 5 -> EjerciciosRBT.ejercicio6a7();
                case 6 -> EjerciciosRBT.ejercicio8();
                case 7 -> EjerciciosRBT.ejercicio9();
                case 8 -> EjerciciosRBT.ejercicio10();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (op != 0);

        sc.close();
    }
}
