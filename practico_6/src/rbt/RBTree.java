package rbt;

public class RBTree<K extends Comparable<K>, V> {
    public final boolean RED = true;
    public final boolean BLACK = false;

    public final RBNode<K,V> NIL;
    public RBNode<K,V> root;

    public RBTree() {
        NIL = new RBNode<>(null, null, BLACK, null);
        NIL.left = NIL.right = NIL.parent = NIL;
        root = NIL;
    }

    /* ===============================
       ROTACIONES
    =============================== */

    public void rotateLeft(RBNode<K,V> x) {
        RBNode<K,V> y = x.right;
        x.right = y.left;
        if (y.left != NIL) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == NIL) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    public void rotateRight(RBNode<K,V> y) {
        RBNode<K,V> x = y.left;
        y.left = x.right;
        if (x.right != NIL) x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == NIL) root = x;
        else if (y == y.parent.left) y.parent.left = x;
        else y.parent.right = x;
        x.right = y;
        y.parent = x;
    }

    /* ===============================
       INSERCIÓN BST (sin fix)
    =============================== */
    public RBNode<K,V> insertBST(K key, V value) {
        RBNode<K,V> z = new RBNode<>(key, value, RED, NIL);
        RBNode<K,V> y = NIL;
        RBNode<K,V> x = root;

        while (x != NIL) {
            y = x;
            if (key.compareTo(x.key) < 0)
                x = x.left;
            else
                x = x.right;
        }

        z.parent = y;
        if (y == NIL)
            root = z;
        else if (key.compareTo(y.key) < 0)
            y.left = z;
        else
            y.right = z;

        z.left = z.right = NIL;
        return z;
    }

    /* ===============================
       FIX DE INSERCIÓN (balanceo)
    =============================== */
    public void fixInsert(RBNode<K,V> z) {
        while (z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                RBNode<K,V> y = z.parent.parent.right;
                if (y.color == RED) {
                    // Tío rojo → recoloreo
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        rotateLeft(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateRight(z.parent.parent);
                }
            } else {
                RBNode<K,V> y = z.parent.parent.left;
                if (y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    public void insert(K key, V value) {
        RBNode<K,V> z = insertBST(key, value);
        fixInsert(z);
    }

    /* ===============================
       SUCESOR / PREDECESOR
    =============================== */
    public RBNode<K,V> successor(RBNode<K,V> x) {
        if (x.right != NIL) return minimo(x.right);
        RBNode<K,V> y = x.parent;
        while (y != NIL && x == y.right) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    public RBNode<K,V> predecessor(RBNode<K,V> x) {
        if (x.left != NIL) return maximo(x.left);
        RBNode<K,V> y = x.parent;
        while (y != NIL && x == y.left) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    public RBNode<K,V> minimo(RBNode<K,V> n) {
        while (n.left != NIL) n = n.left;
        return n;
    }

    public RBNode<K,V> maximo(RBNode<K,V> n) {
        while (n.right != NIL) n = n.right;
        return n;
    }

    /* ===============================
       CONSULTA POR RANGO [a,b]
    =============================== */
    public void rangeSearch(K a, K b) {
        System.out.print("Claves en ["+a+","+b+"]: ");
        rangeRec(root, a, b);
        System.out.println();
    }

    private void rangeRec(RBNode<K,V> n, K a, K b) {
        if (n == NIL) return;
        if (n.key.compareTo(a) > 0)
            rangeRec(n.left, a, b);
        if (n.key.compareTo(a) >= 0 && n.key.compareTo(b) <= 0)
            System.out.print(n.key + " ");
        if (n.key.compareTo(b) < 0)
            rangeRec(n.right, a, b);
    }

    /* ===============================
       IMPRESIÓN EN ORDEN
    =============================== */
    public void inorder() {
        inorderRec(root);
        System.out.println();
    }

    private void inorderRec(RBNode<K,V> n) {
        if (n != NIL) {
            inorderRec(n.left);
            System.out.print(n.key + (n.color ? "(R) " : "(N) "));
            inorderRec(n.right);
        }
    }
}
