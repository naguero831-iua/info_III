package avl;

/**
 * Implementación de un Árbol Binario de Búsqueda (ABB).
 * Ofrece inserción, búsqueda, recorridos y visualización ASCII.
 */
public class Abb {

    private NodoAVL root;

    // ---------------- ALTURA ----------------
    /** Devuelve la altura del nodo (0 si es null). */
    public int height(NodoAVL n) {
        return (n == null) ? 0 : n.getAltura();
    }

    /** Actualiza la altura de un nodo según sus hijos. */
    private void updateHeight(NodoAVL n) {
        if (n != null) {
            n.setAltura(1 + Math.max(height(n.getIzq()), height(n.getDer())));
        }
    }

    // ---------------- FACTOR DE EQUILIBRIO ----------------
    /** Devuelve el factor de equilibrio de un nodo dado (izq - der). */
    public int getBalance(NodoAVL n) {
        if (n == null) return 0;
        return height(n.getIzq()) - height(n.getDer());
    }

    /** Devuelve el factor de equilibrio de la raíz. */
    public int getRootBalance() {
        return getBalance(root);
    }

    // ---------------- INSERCIÓN ----------------
    public void insert(int key) {
        root = insert(root, key);
    }

    private NodoAVL insert(NodoAVL node, int key) {
        if (node == null) return new NodoAVL(key);

        if (key < node.getDato()) {
            node.setIzq(insert(node.getIzq(), key));
        } else if (key > node.getDato()) {
            node.setDer(insert(node.getDer(), key));
        } else {
            return node; // duplicados no permitidos
        }

        updateHeight(node);
        return node;
    }

    // ---------------- BÚSQUEDA ----------------
    public boolean contains(int key) {
        return contains(root, key);
    }

    private boolean contains(NodoAVL n, int key) {
        if (n == null) return false;
        if (key == n.getDato()) return true;
        return key < n.getDato()
                ? contains(n.getIzq(), key)
                : contains(n.getDer(), key);
    }

    // ---------------- GETTERS ----------------
    public NodoAVL getRoot() {
        return root;
    }

    /** Devuelve la altura total del árbol. */
    public int getAlturaTotal() {
        return height(root);
    }

    // ---------------- RECORRIDOS ----------------
    public void inorder() {
        System.out.print("Inorden: ");
        inorder(root);
        System.out.println();
    }

    private void inorder(NodoAVL n) {
        if (n != null) {
            inorder(n.getIzq());
            System.out.print(n.getDato() + " ");
            inorder(n.getDer());
        }
    }

    public void preorder() {
        System.out.print("Preorden: ");
        preorder(root);
        System.out.println();
    }

    private void preorder(NodoAVL n) {
        if (n != null) {
            System.out.print(n.getDato() + " ");
            preorder(n.getIzq());
            preorder(n.getDer());
        }
    }

    public void postorder() {
        System.out.print("Postorden: ");
        postorder(root);
        System.out.println();
    }

    private void postorder(NodoAVL n) {
        if (n != null) {
            postorder(n.getIzq());
            postorder(n.getDer());
            System.out.print(n.getDato() + " ");
        }
    }

    // ---------------- IMPRESIÓN EN FORMATO ÁRBOL (ASCII) ----------------
    public void printTree() {
        printTree(root, "", true);
    }

    private void printTree(NodoAVL node, String prefix, boolean isLeft) {
        if (node == null) return;

        if (node.getDer() != null) {
            printTree(node.getDer(), prefix + "    ", false);
        }

        System.out.println(prefix + (isLeft ? "└── " : "┌── ") + node.getDato());

        if (node.getIzq() != null) {
            printTree(node.getIzq(), prefix + "    ", true);
        }
    }
}
