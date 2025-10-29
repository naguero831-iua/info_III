package rbt;

public class EjerciciosRBT {

    public static void ejercicio1() {
        System.out.println("Ejercicio 1 - Nodo y NIL sentinel");
        RBTree<Integer,String> t = new RBTree<>();
        System.out.println("Root inicial apunta a NIL negro: " + (t.root == t.NIL));
    }

    public static void ejercicio2y3() {
        System.out.println("Ejercicio 2 y 3 - Rotaciones");
        RBTree<Integer,String> t = new RBTree<>();
        RBNode<Integer,String> x = t.insertBST(10,"A");
        RBNode<Integer,String> y = t.insertBST(20,"B");
        x.right = y; y.parent = x;
        t.root = x;
        System.out.println("Antes de rotar izquierda:");
        t.inorder();
        t.rotateLeft(x);
        System.out.println("Después de rotar izquierda:");
        t.inorder();
    }

    public static void ejercicio4() {
        System.out.println("Ejercicio 4 - Inserción BST sin balance");
        RBTree<Integer,String> t = new RBTree<>();
        t.insertBST(10,"A");
        t.insertBST(20,"B");
        t.insertBST(15,"C");
        t.inorder();
    }

    public static void ejercicio5() {
        System.out.println("Ejercicio 5 - Clasificador de casos");
        RBTree<Integer,String> t = new RBTree<>();
        RBNode<Integer,String> z = t.insertBST(10,"A");
        RBNode<Integer,String> p = t.insertBST(5,"B");
        z.left = p; p.parent = z;
        System.out.println("Caso detectado: " + RBUtil.clasificar(p, t.NIL));
    }

    public static void ejercicio6a7() {
        System.out.println("Ejercicio 6 y 7 - Insert y fixInsert");
        RBTree<Integer,String> t = new RBTree<>();
        int[] seq = {10, 5, 15, 1, 6, 12, 18, 3};
        for (int x : seq) t.insert(x,"v"+x);
        t.inorder();
    }

    public static void ejercicio8() {
        System.out.println("Ejercicio 8 - Successor y Predecessor");
        RBTree<Integer,String> t = new RBTree<>();
        int[] seq = {5,10,15};
        for (int x : seq) t.insert(x,"v"+x);
        RBNode<Integer,String> nodo10 = t.root.right;
        System.out.println("Successor(10): " + t.successor(nodo10).key);
        System.out.println("Predecessor(10): " + t.predecessor(nodo10).key);
    }

    public static void ejercicio9() {
        System.out.println("Ejercicio 9 - Consulta por rango");
        RBTree<Integer,String> t = new RBTree<>();
        for (int i=1;i<=20;i++) t.insert(i,"v"+i);
        t.rangeSearch(5,12);
    }

    public static void ejercicio10() {
        System.out.println("Ejercicio 10 - Verificadores de invariantes");
        RBTree<Integer,String> t = new RBTree<>();
        int[] seq = {10,20,30,40,50,25};
        for (int x : seq) t.insert(x,"v"+x);
        RBChecker<Integer,String> chk = new RBChecker<>(t);
        chk.verificar();
    }
}
