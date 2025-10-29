package avl;

public class ArbolAVL {
    public NodoAVL raiz;

    public int altura(NodoAVL n) {
        return (n == null) ? 0 : n.altura;
    }

    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

    // Rotación simple a la derecha
    public NodoAVL rotacionDerecha(NodoAVL y) {
        NodoAVL x = y.izq;
        NodoAVL T2 = x.der;

        x.der = y;
        y.izq = T2;

        y.altura = max(altura(y.izq), altura(y.der)) + 1;
        x.altura = max(altura(x.izq), altura(x.der)) + 1;

        return x;
    }

    // Rotación simple a la izquierda
    public NodoAVL rotacionIzquierda(NodoAVL x) {
        NodoAVL y = x.der;
        NodoAVL T2 = y.izq;

        y.izq = x;
        x.der = T2;

        x.altura = max(altura(x.izq), altura(x.der)) + 1;
        y.altura = max(altura(y.izq), altura(y.der)) + 1;

        return y;
    }

    public NodoAVL insertar(NodoAVL nodo, int valor) {
        if (nodo == null)
            return new NodoAVL(valor);

        if (valor < nodo.valor)
            nodo.izq = insertar(nodo.izq, valor);
        else if (valor > nodo.valor)
            nodo.der = insertar(nodo.der, valor);
        else
            return nodo; // sin duplicados

        nodo.altura = 1 + max(altura(nodo.izq), altura(nodo.der));

        int fe = nodo.getFactorEquilibrio();

        // Casos de rotación
        if (fe > 1 && valor < nodo.izq.valor)
            return rotacionDerecha(nodo); // LL

        if (fe < -1 && valor > nodo.der.valor)
            return rotacionIzquierda(nodo); // RR

        if (fe > 1 && valor > nodo.izq.valor) { // LR
            nodo.izq = rotacionIzquierda(nodo.izq);
            return rotacionDerecha(nodo);
        }

        if (fe < -1 && valor < nodo.der.valor) { // RL
            nodo.der = rotacionDerecha(nodo.der);
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    public void insertar(int valor) {
        raiz = insertar(raiz, valor);
    }

    // Mínimo valor
    private NodoAVL minimo(NodoAVL nodo) {
        NodoAVL actual = nodo;
        while (actual.izq != null)
            actual = actual.izq;
        return actual;
    }

    public NodoAVL eliminar(NodoAVL nodo, int valor) {
        if (nodo == null) return nodo;

        if (valor < nodo.valor)
            nodo.izq = eliminar(nodo.izq, valor);
        else if (valor > nodo.valor)
            nodo.der = eliminar(nodo.der, valor);
        else {
            if ((nodo.izq == null) || (nodo.der == null)) {
                NodoAVL temp = (nodo.izq != null) ? nodo.izq : nodo.der;
                if (temp == null) nodo = null;
                else nodo = temp;
            } else {
                NodoAVL temp = minimo(nodo.der);
                nodo.valor = temp.valor;
                nodo.der = eliminar(nodo.der, temp.valor);
            }
        }

        if (nodo == null) return nodo;

        nodo.altura = 1 + max(altura(nodo.izq), altura(nodo.der));
        int fe = nodo.getFactorEquilibrio();

        // Rebalanceos
        if (fe > 1 && nodo.izq.getFactorEquilibrio() >= 0)
            return rotacionDerecha(nodo);

        if (fe > 1 && nodo.izq.getFactorEquilibrio() < 0) {
            nodo.izq = rotacionIzquierda(nodo.izq);
            return rotacionDerecha(nodo);
        }

        if (fe < -1 && nodo.der.getFactorEquilibrio() <= 0)
            return rotacionIzquierda(nodo);

        if (fe < -1 && nodo.der.getFactorEquilibrio() > 0) {
            nodo.der = rotacionDerecha(nodo.der);
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    public void eliminar(int valor) {
        raiz = eliminar(raiz, valor);
    }

    // Mostrar árbol en orden
    public void mostrarInOrder(NodoAVL nodo) {
        if (nodo != null) {
            mostrarInOrder(nodo.izq);
            System.out.print(nodo.valor + " ");
            mostrarInOrder(nodo.der);
        }
    }

    public void mostrar() {
        mostrarInOrder(raiz);
        System.out.println();
    }
}
