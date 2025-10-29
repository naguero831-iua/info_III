package avl;

public class NodoAVL {
    public int valor;
    public NodoAVL izq;
    public NodoAVL der;
    public int altura;

    public NodoAVL(int valor) {
        this.valor = valor;
        this.altura = 1;
    }

    public int getFactorEquilibrio() {
        int altIzq = (izq == null) ? 0 : izq.altura;
        int altDer = (der == null) ? 0 : der.altura;
        return altIzq - altDer;
    }
}
