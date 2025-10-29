package main;

import java.util.Scanner;
import avl.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("========= MENÚ TP ÁRBOL AVL =========");
            System.out.println("1. Ejercicio 1 - LL y RR");
            System.out.println("2. Ejercicio 2 - LR y RL");
            System.out.println("3. Ejercicio 3 - Efecto Peinar");
            System.out.println("4. Ejercicio 4 - Eliminación con rebalanceo");
            System.out.println("5. Ejercicio 5 - Verificar AVL");
            System.out.println("6. Ejercicio 6 - Factores de equilibrio");
            System.out.println("7. Ejercicio 7 - Rotación izquierda");
            System.out.println("8. Ejercicio 8 - Rotación doble LR");
            System.out.println("9. Ejercicio 9 - Costos y altura");
            System.out.println("10. Ejercicio 10 - Secuencias estresantes");
            System.out.println("0. Salir");
            System.out.print("Elija una opción: ");
            opcion = sc.nextInt();

            switch (opcion) {
                case 1 -> EjerciciosAVL.ejercicio1();
                case 2 -> EjerciciosAVL.ejercicio2();
                case 3 -> EjerciciosAVL.ejercicio3();
                case 4 -> EjerciciosAVL.ejercicio4();
                case 5 -> EjerciciosAVL.ejercicio5();
                case 6 -> EjerciciosAVL.ejercicio6();
                case 7 -> EjerciciosAVL.ejercicio7();
                case 8 -> EjerciciosAVL.ejercicio8();
                case 9 -> EjerciciosAVL.ejercicio9();
                case 10 -> EjerciciosAVL.ejercicio10();
                case 0 -> System.out.println("Fin del programa.");
                default -> System.out.println("Opción inválida.");
            }
            System.out.println();
        } while (opcion != 0);

        sc.close();
    }
}
