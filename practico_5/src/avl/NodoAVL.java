package avl;

public class NodoAVL {
    private int dato;
    private int altura;
    private NodoAVL izq, der;
    private int bal;

    public NodoAVL(int dato) {
        this.dato = dato;
        this.altura = 1;
        this.izq = null;
        this.der = null;
        this.bal = 0;
    }

    public int getDato() {
        return dato;
    }

    public void setDato(int dato) {
        this.dato = dato;
    }

    public NodoAVL getIzq() {
        return izq;
    }

    public void setIzq(NodoAVL izq) {
        this.izq = izq;
    }

    public NodoAVL getDer() {
        return der;
    }

    public void setDer(NodoAVL der) {
        this.der = der;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getBal() {
        return bal;
    }

    public void setBal(int bal) {
        this.bal = bal;
    }
}
