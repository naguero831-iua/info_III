package avl;

public class Utilidades {
    public static void insertarSecuencia(ArbolAVL arbol, int[] valores) {
        for (int v : valores)
            arbol.insertar(v);
    }

    public static void eliminarSecuencia(ArbolAVL arbol, int[] valores) {
        for (int v : valores)
            arbol.eliminar(v);
    }
    public static void mostrarFactores(NodoAVL n) {
        if (n != null) {
            mostrarFactores(n.izq);
            System.out.println("Nodo " + n.valor + " | Altura: " + n.altura + " | FE: " + n.getFactorEquilibrio());
            mostrarFactores(n.der);
        }
    }
}
