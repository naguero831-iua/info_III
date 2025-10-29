package avl;

public class EjerciciosAVL {

    // Ejercicio 1
    public static void ejercicio1() {
        System.out.println("Ejercicio 1: Inserciones caso LL y RR");
        ArbolAVL a = new ArbolAVL();
        int[] seq = {30, 20, 10, 40, 50, 60};
        Utilidades.insertarSecuencia(a, seq);
        a.mostrar();
    }

    // Ejercicio 2
    public static void ejercicio2() {
        System.out.println("Ejercicio 2: Rotaciones dobles LR y RL");
        ArbolAVL a = new ArbolAVL();
        int[] seq = {30, 10, 20, 40, 35, 37};
        Utilidades.insertarSecuencia(a, seq);
        a.mostrar();
    }

    // Ejercicio 3
    public static void ejercicio3() {
        System.out.println("Ejercicio 3: Efecto peinar (ABB vs AVL)");
        ArbolAVL a = new ArbolAVL();
        int[] seq = {5, 10, 15, 20, 25, 30, 35};
        Utilidades.insertarSecuencia(a, seq);
        a.mostrar();
        System.out.println("El AVL mantiene altura O(log n) gracias a rotaciones.");
    }

    // Ejercicio 4
    public static void ejercicio4() {
        System.out.println("Ejercicio 4: Eliminación con rebalanceo");
        ArbolAVL a = new ArbolAVL();
        int[] seq = {50, 30, 70, 20, 40, 60, 80, 65, 75};
        Utilidades.insertarSecuencia(a, seq);
        a.eliminar(20);
        a.eliminar(70);
        a.mostrar();
    }

    // Ejercicio 5
    public static void ejercicio5() {
        System.out.println("Ejercicio 5: Verificar AVL");
        ArbolAVL a = new ArbolAVL();
        int[] seq = {10, 20, 30};
        Utilidades.insertarSecuencia(a, seq);
        AVLChecker.Resultado r = AVLChecker.esAVL(a.raiz);
        System.out.println("¿Es AVL?: " + r.esAVL + " | Altura total: " + r.altura);
    }

    // Ejercicio 6
    public static void ejercicio6() {
        System.out.println("Ejercicio 6: Factores de equilibrio");
        ArbolAVL a = new ArbolAVL();
        int[] seq = {10, 100, 20, 80, 40, 70};
        Utilidades.insertarSecuencia(a, seq);
        Utilidades.mostrarFactores(a.raiz);
    }

    // Ejercicio 7
    public static void ejercicio7() {
        System.out.println("Ejercicio 7: Rotación izquierda demostrada (caso RR)");
        ArbolAVL a = new ArbolAVL();
        int[] seq = {10, 20, 30};
        Utilidades.insertarSecuencia(a, seq);
        a.mostrar();
    }

    // Ejercicio 8
    public static void ejercicio8() {
        System.out.println("Ejercicio 8: Rotación doble Izquierda-Derecha (LR)");
        ArbolAVL a = new ArbolAVL();
        int[] seq = {30, 10, 20};
        Utilidades.insertarSecuencia(a, seq);
        a.mostrar();
    }

    // Ejercicio 9
    public static void ejercicio9() {
        System.out.println("Ejercicio 9: Altura O(log n) y costo de operaciones");
        System.out.println("Un AVL mantiene altura O(log n), por lo tanto las operaciones de búsqueda, inserción y eliminación son O(log n).");
        System.out.println("Comparado con un ABB sin balance: puede degradar a O(n).");
        System.out.println("En cambio, los árboles rojo-negro garantizan también O(log n) pero con menos rotaciones promedio.");
    }

    // Ejercicio 10
    public static void ejercicio10() {
        System.out.println("Ejercicio 10: Secuencias estresantes");
        int[] creciente = new int[20];
        int[] decreciente = new int[20];
        int[] pseudo = {5, 3, 8, 2, 7, 1, 9, 5, 4, 10, 6, 8, 11, 3, 12, 7, 13, 2, 14, 15};

        for (int i = 0; i < 20; i++) {
            creciente[i] = i + 1;
            decreciente[i] = 20 - i;
        }

        System.out.println("➡ Secuencia creciente:");
        testSecuencia(creciente);
        System.out.println("\n➡ Secuencia decreciente:");
        testSecuencia(decreciente);
        System.out.println("\n➡ Secuencia pseudoaleatoria:");
        testSecuencia(pseudo);
    }

    private static void testSecuencia(int[] seq) {
        ArbolAVL a = new ArbolAVL();
        for (int v : seq)
            a.insertar(v);
        AVLChecker.Resultado r = AVLChecker.esAVL(a.raiz);
        System.out.println("¿AVL válido? " + r.esAVL + " | Altura: " + r.altura);
    }
}
