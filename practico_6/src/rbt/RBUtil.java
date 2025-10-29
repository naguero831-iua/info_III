package rbt;

public class RBUtil {

    // Clasifica el caso (ejercicio 5)
    public static <K extends Comparable<K>,V> RBCaso clasificar(RBNode<K,V> z, RBNode<K,V> NIL) {
        if (z == NIL || z.parent == NIL || z.parent.parent == NIL) return RBCaso.NINGUNO;
        RBNode<K,V> p = z.parent;
        RBNode<K,V> g = p.parent;
        RBNode<K,V> tio = (p == g.left) ? g.right : g.left;

        if (tio.color) return RBCaso.TIO_ROJO;

        if (p == g.left && z == p.left) return RBCaso.LL;
        if (p == g.right && z == p.right) return RBCaso.RR;
        if (p == g.left && z == p.right) return RBCaso.LR;
        if (p == g.right && z == p.left) return RBCaso.RL;

        return RBCaso.NINGUNO;
    }
}
