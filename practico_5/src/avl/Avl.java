package avl;

/**
 * Clase Avl: Árbol binario de búsqueda auto-balanceado (AVL).
 * Soporta inserción, eliminación, búsqueda, recorridos y rotaciones.
 * Mantiene un contador de rotaciones para estadísticas.
 */
public class Avl {

    private NodoAVL root;              // Raíz del árbol
    private int rotacionesTotales = 0; // Contador de rotaciones

    /** ---------------- GETTERS ---------------- **/
    public NodoAVL getRoot() { return root; }
    public int getAlturaTotal() { return height(root); }
    public int getRootBalance() { return getBalance(root); }
    public int getRotaciones() { return rotacionesTotales; }

    /** ---------------- MÉTODOS AUXILIARES ---------------- **/
    // Altura de un nodo
    public int height(NodoAVL n) {
        return (n == null) ? 0 : n.getAltura();
    }

    // Actualiza altura de un nodo según hijos
    private void updateHeight(NodoAVL n) {
        if (n != null) {
            n.setAltura(1 + Math.max(height(n.getIzq()), height(n.getDer())));
        }
    }

    // Factor de equilibrio: altura del hijo izquierdo - altura del hijo derecho
    public int getBalance(NodoAVL n) {
        return (n == null) ? 0 : height(n.getIzq()) - height(n.getDer());
    }

    /** ---------------- ROTACIONES ---------------- **/
    private NodoAVL rightRotate(NodoAVL y) {
        rotacionesTotales++;
        NodoAVL x = y.getIzq();
        NodoAVL T2 = x.getDer();

        x.setDer(y);
        y.setIzq(T2);

        updateHeight(y);
        updateHeight(x);

        System.out.println("Rotación derecha en nodo: " + x.getDato());
        return x;
    }

    private NodoAVL leftRotate(NodoAVL x) {
        rotacionesTotales++;
        NodoAVL y = x.getDer();
        NodoAVL T2 = y.getIzq();

        y.setIzq(x);
        x.setDer(T2);

        updateHeight(x);
        updateHeight(y);

        System.out.println("Rotación izquierda en nodo: " + x.getDato());
        return y;
    }

    /** ---------------- REBALANCE ---------------- **/
    private NodoAVL rebalance(NodoAVL node) {
        int balance = getBalance(node);

        if (balance >= 2 || balance <= -2) {
            System.out.println("Nodo crítico detectado: " + node.getDato() + ", FE=" + balance);
        }

        if (balance > 1 && getBalance(node.getIzq()) >= 0) return rightRotate(node); // LL
        if (balance > 1 && getBalance(node.getIzq()) < 0) {                          // LR
            System.out.println("Rotación LR en nodo: " + node.getDato());
            node.setIzq(leftRotate(node.getIzq()));
            return rightRotate(node);
        }
        if (balance < -1 && getBalance(node.getDer()) <= 0) return leftRotate(node); // RR
        if (balance < -1 && getBalance(node.getDer()) > 0) {                          // RL
            System.out.println("Rotación RL en nodo: " + node.getDato());
            node.setDer(rightRotate(node.getDer()));
            return leftRotate(node);
        }

        return node;
    }

    /** ---------------- INSERCIÓN ---------------- **/
    public void insert(int key) {
        root = insert(root, key);
    }

    private NodoAVL insert(NodoAVL node, int key) {
        if (node == null) return new NodoAVL(key);

        if (key < node.getDato()) node.setIzq(insert(node.getIzq(), key));
        else if (key > node.getDato()) node.setDer(insert(node.getDer(), key));
        else return node; // Duplicados no permitidos

        updateHeight(node);
        return rebalance(node);
    }

    /** ---------------- ELIMINACIÓN ---------------- **/
    public void delete(int key) {
        root = deleteNode(root, key);
    }

    private NodoAVL deleteNode(NodoAVL node, int key) {
        if (node == null) return null;

        if (key < node.getDato()) node.setIzq(deleteNode(node.getIzq(), key));
        else if (key > node.getDato()) node.setDer(deleteNode(node.getDer(), key));
        else {
            // Nodo encontrado
            if (node.getIzq() == null) return node.getDer();
            if (node.getDer() == null) return node.getIzq();

            // Dos hijos: reemplazar con sucesor inorden
            NodoAVL temp = node.getDer();
            while (temp.getIzq() != null) temp = temp.getIzq();
            node.setDato(temp.getDato());
            node.setDer(deleteNode(node.getDer(), temp.getDato()));
        }

        updateHeight(node);
        return rebalance(node);
    }

    /** ---------------- BÚSQUEDA ---------------- **/
    public boolean contains(int key) {
        NodoAVL n = root;
        while (n != null) {
            if (key == n.getDato()) return true;
            n = (key < n.getDato()) ? n.getIzq() : n.getDer();
        }
        return false;
    }

    /** ---------------- RECORRIDOS ---------------- **/
    public void inorder() { traverseInorder(root); System.out.println(); }
    private void traverseInorder(NodoAVL n) {
        if (n != null) {
            traverseInorder(n.getIzq());
            System.out.print(n.getDato() + " ");
            traverseInorder(n.getDer());
        }
    }

    public void preorder() { traversePreorder(root); System.out.println(); }
    private void traversePreorder(NodoAVL n) {
        if (n != null) {
            System.out.print(n.getDato() + " ");
            traversePreorder(n.getIzq());
            traversePreorder(n.getDer());
        }
    }

    public void postorder() { traversePostorder(root); System.out.println(); }
    private void traversePostorder(NodoAVL n) {
        if (n != null) {
            traversePostorder(n.getIzq());
            traversePostorder(n.getDer());
            System.out.print(n.getDato() + " ");
        }
    }

    /** ---------------- IMPRESIÓN ASCII ---------------- **/
    public void printTree() { printTree(root, "", true); }
    private void printTree(NodoAVL node, String prefix, boolean isLeft) {
        if (node == null) return;
        if (node.getDer() != null) printTree(node.getDer(), prefix + "    ", false);
        System.out.println(prefix + (isLeft ? "└── " : "┌── ") + node.getDato());
        if (node.getIzq() != null) printTree(node.getIzq(), prefix + "    ", true);
    }
}
