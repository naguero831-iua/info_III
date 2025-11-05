package avl;

public class Ejercicio5 {
    public static void main(String[] args) {
        Avl arbol = new Avl();
        Abb arbol2 = new Abb();
        int[] valores = {30, 20, 10, 40, 50, 60, 125, 70};

        for (int v : valores) {
            arbol.insert(v);
            arbol2.insert(v);
        }

        ResultadoAVL res1 = esAVL(arbol.getRoot());
        System.out.println("Es AVL? " + res1.esAVL + ", Altura: " + res1.altura);

        ResultadoAVL res2 = esAVL(arbol2.getRoot());
        System.out.println("Es AVL? " + res2.esAVL + ", Altura: " + res2.altura);
    }

    // Clase auxiliar para devolver esAVL y altura
    static class ResultadoAVL {
        boolean esAVL;
        int altura;
        ResultadoAVL(boolean esAVL, int altura) {
            this.esAVL = esAVL;
            this.altura = altura;
        }
    }

    public static ResultadoAVL esAVL(NodoAVL r) {
        if (r == null) return new ResultadoAVL(true, 0);

        ResultadoAVL izq = esAVL(r.getIzq());
        ResultadoAVL der = esAVL(r.getDer());


        boolean esAVLactual = izq.esAVL && der.esAVL && Math.abs(izq.altura - der.altura) <= 1;

        int alturaActual = 1 + Math.max(izq.altura, der.altura);

        return new ResultadoAVL(esAVLactual, alturaActual);
    }
}
