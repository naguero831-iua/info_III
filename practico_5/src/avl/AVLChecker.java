package avl;

public class AVLChecker {

    public static class Resultado {
        public boolean esAVL;
        public int altura;
        public Resultado(boolean esAVL, int altura) {
            this.esAVL = esAVL;
            this.altura = altura;
        }
    }

    public static Resultado esAVL(NodoAVL r) {
        if (r == null) return new Resultado(true, 0);

        Resultado izq = esAVL(r.izq);
        Resultado der = esAVL(r.der);

        boolean cumple = izq.esAVL && der.esAVL && Math.abs(izq.altura - der.altura) <= 1;
        int alt = Math.max(izq.altura, der.altura) + 1;

        if (r.izq != null && r.izq.valor >= r.valor) cumple = false;
        if (r.der != null && r.der.valor <= r.valor) cumple = false;

        return new Resultado(cumple, alt);
    }
}
