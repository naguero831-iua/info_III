package rbt;

public class RBChecker<K extends Comparable<K>,V> {
    private final RBTree<K,V> tree;

    public RBChecker(RBTree<K,V> tree) {
        this.tree = tree;
    }

    public boolean raizNegra() {
        return tree.root.color == tree.BLACK;
    }

    public boolean sinRojoRojo(RBNode<K,V> n) {
        if (n == tree.NIL) return true;
        if (n.color == tree.RED) {
            if (n.left.color == tree.RED || n.right.color == tree.RED)
                return false;
        }
        return sinRojoRojo(n.left) && sinRojoRojo(n.right);
    }

    public int alturaNegra(RBNode<K,V> n) {
        if (n == tree.NIL) return 1;
        int izq = alturaNegra(n.left);
        int der = alturaNegra(n.right);
        if (izq == 0 || der == 0 || izq != der) return 0;
        return izq + (n.color == tree.BLACK ? 1 : 0);
    }

    public void verificar() {
        System.out.println("Raíz negra: " + raizNegra());
        System.out.println("Sin rojo-rojo: " + sinRojoRojo(tree.root));
        System.out.println("Altura negra válida: " + (alturaNegra(tree.root) != 0));
    }
}
