package avl;

public class Ejercicio10 {
    public static void main(String[] args) {
        // ---- Secuencias de prueba ----
        int[][] secuencias = {
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20},
            {20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1},
            {10, 5, 15, 3, 7, 12, 18, 1, 4, 6, 9, 11, 13, 16, 19, 2, 8, 14, 17, 20}
        };
        String[] nombres = {"Creciente", "Decreciente", "Pseudoaleatoria"};

        // ---- Iterar sobre cada secuencia ----
        for (int i = 0; i < secuencias.length; i++) {
            Avl arbol = new Avl();  // nuevo árbol para cada secuencia
            System.out.println("===== Secuencia " + nombres[i] + " =====");

            for (int n : secuencias[i]) {
                System.out.println("\nInsertando: " + n);
                arbol.insert(n);
                arbol.printTree();

                // ---- Test 1: el árbol sigue siendo AVL ----
                if (!esAVL(arbol.getRoot())) {
                    System.out.println("❌ Árbol dejó de ser AVL tras insertar " + n);
                    break;
                }

                // ---- Test 2: recorrido inorder creciente ----
                if (!esOrdenado(arbol.getRoot(), Integer.MIN_VALUE, Integer.MAX_VALUE)) {
                    System.out.println("❌ Árbol no está ordenado tras insertar " + n);
                    break;
                }

                // ---- Test 3: factor de equilibrio ----
                if (!factorEquilibrado(arbol.getRoot())) {
                    System.out.println("❌ Factor de equilibrio incorrecto tras insertar " + n);
                    break;
                }
            }

            System.out.println("✅ Prueba completada.");
            System.out.println("Rotaciones totales: " + arbol.getRotaciones());
            System.out.println();
        }
    }

    // ---- Funciones auxiliares ----
    public static boolean esAVL(NodoAVL n) {
        if (n == null) return true;
        int fe = Math.abs(new Avl().getBalance(n));
        if (fe > 1) return false;
        return esAVL(n.getIzq()) && esAVL(n.getDer());
    }
    
    public static boolean esOrdenado(NodoAVL n, int min, int max) {
        if (n == null) return true;
        if (n.getDato() <= min || n.getDato() >= max) return false;
        return esOrdenado(n.getIzq(), min, n.getDato()) &&
               esOrdenado(n.getDer(), n.getDato(), max);
    }

    public static boolean factorEquilibrado(NodoAVL n) {
        if (n == null) return true;
        int fe = Math.abs(new Avl().getBalance(n));
        if (fe > 1) return false;
        return factorEquilibrado(n.getIzq()) && factorEquilibrado(n.getDer());
    }
}
