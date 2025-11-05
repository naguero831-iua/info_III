package integrador.agenda;

import integrador.model.Turno;
import java.time.LocalDateTime;
import java.util.Optional;

/*
 Agenda implementada con AVL por Turno.fechaHora.
 Permite agendar (rechaza solapamientos), cancelar por id, siguiente, primerHueco.
*/

public class AVLAgenda {

    static class Node {
        Turno t; Node izq, der; int h;
        Node(Turno t) { this.t = t; izq = der = null; h = 1; }
    }

    private Node root;

    // Exponer el nodo raíz para AgendaConHistorial
    public Object agendaRoot() { return root; }

    private int height(Node n) { return n==null?0:n.h; }
    private void upd(Node n) { if (n!=null) n.h = 1 + Math.max(height(n.izq), height(n.der)); }
    private int balance(Node n) { return n==null?0:height(n.izq)-height(n.der); }

    private Node rotRight(Node y) {
        Node x = y.izq; Node T2 = x.der;
        x.der = y; y.izq = T2;
        upd(y); upd(x); return x;
    }
    private Node rotLeft(Node x) {
        Node y = x.der; Node T2 = y.izq;
        y.izq = x; x.der = T2;
        upd(x); upd(y); return y;
    }

    private boolean overlaps(Turno a, Turno b) {
        return a.fechaHora.isBefore(b.fin()) && b.fechaHora.isBefore(a.fin());
    }

    public boolean agendar(Turno t) {
        if (t == null || t.duracionMin <= 0) return false;
        if (t.fechaHora.isBefore(LocalDateTime.now())) return false;
        if (containsOverlap(root, t)) return false;
        root = insertRec(root, t);
        return true;
    }

    private Node insertRec(Node node, Turno t) {
        // 1. Inserción estándar de un BST.
        if (node == null) return new Node(t);
        if (t.compareTo(node.t) < 0) node.izq = insertRec(node.izq, t);
        else node.der = insertRec(node.der, t);
        upd(node);
        int b = balance(node);
        if (b > 1 && t.compareTo(node.izq.t) < 0) return rotRight(node);
        if (b < -1 && t.compareTo(node.der.t) > 0) return rotLeft(node);
        if (b > 1 && t.compareTo(node.izq.t) > 0) { node.izq = rotLeft(node.izq); return rotRight(node); }
        if (b < -1 && t.compareTo(node.der.t) < 0) { node.der = rotRight(node.der); return rotLeft(node); }
        return node;
    }

    private boolean containsOverlap(Node node, Turno t) {
        if (node == null) return false;
        if (overlaps(node.t, t)) return true;
        if (t.fin().isBefore(node.t.fechaHora)) return containsOverlap(node.izq, t);
        if (t.fechaHora.isAfter(node.t.fin())) return containsOverlap(node.der, t);
        // Else need to check both sides
        return containsOverlap(node.izq, t) || containsOverlap(node.der, t);
    }

    public boolean cancelar(String idTurno) {
        if (!containsId(root, idTurno)) return false; // El turno no existe.
        root = deleteById(root, idTurno);
        return true;
    }

    private boolean containsId(Node n, String id) {
        if (n == null) return false;
        if (n.t.id.equals(id)) return true;
        return containsId(n.izq, id) || containsId(n.der, id);
    }

    private Node deleteById(Node node, String id) {
        if (node == null) return null;
        if (node.t.id.equals(id)) {
            if (node.izq == null || node.der == null) {
                Node tmp = node.izq != null ? node.izq : node.der;
                return tmp;
            } else {
                Node succ = minNode(node.der);
                node.t = succ.t;
                node.der = deleteById(node.der, succ.t.id);
            }
        } else {
            // Continuar la búsqueda en los subárboles.
            node.izq = deleteById(node.izq, id);
            node.der = deleteById(node.der, id);
        }
        upd(node);
        int b = balance(node);
        if (b > 1 && balance(node.izq) >= 0) return rotRight(node);
        if (b > 1 && balance(node.izq) < 0) { node.izq = rotLeft(node.izq); return rotRight(node); }
        if (b < -1 && balance(node.der) <= 0) return rotLeft(node);
        if (b < -1 && balance(node.der) > 0) { node.der = rotRight(node.der); return rotLeft(node); }
        return node;
    }

    private Node minNode(Node n) { while (n.izq != null) n = n.izq; return n; }

    public Optional<Turno> siguiente(LocalDateTime t) {
        Node cur = root; Turno best = null;
        while (cur != null) {
            // Si el turno actual no es anterior a `t`.
            if (!cur.t.fechaHora.isBefore(t)) {
                if (best == null || cur.t.fechaHora.isBefore(best.fechaHora)) best = cur.t;
                cur = cur.izq;
            } else cur = cur.der;
        }

        return Optional.ofNullable(best);
    }

    public Optional<Turno> primerHueco(LocalDateTime t0, int durMin) {
        // find first >= t0
        Node cur = root; Node bestNode = null;
        while (cur != null) {
            if (!cur.t.fechaHora.isBefore(t0)) { bestNode = cur; cur = cur.izq; }
            else cur = cur.der;
        }
        LocalDateTime candidate = t0;
        if (bestNode == null) return Optional.of(new Turno("GAP", "", "", candidate, durMin, "hueco"));
        // check gap before first
        if (!candidate.plusMinutes(durMin).isAfter(bestNode.t.fechaHora)) return Optional.of(new Turno("GAP", "", "", candidate, durMin, "hueco"));
        // iterate successors by performing in-order traversal keeping previous
        Turno[] buffer = toArray(); // convert to array ordered
        for (int i=0;i<buffer.length-1;i++) {
            LocalDateTime end = buffer[i].fin();
            if (!end.plusMinutes(durMin).isAfter(buffer[i+1].fechaHora)) return Optional.of(new Turno("GAP","", "", end, durMin, "hueco"));
        }
        // after last
        if (buffer.length>0) return Optional.of(new Turno("GAP","", "", buffer[buffer.length-1].fin(), durMin, "hueco"));
        return Optional.of(new Turno("GAP","", "", candidate, durMin, "hueco"));
    }

    // Convert tree to array inorder
    private Turno[] toArray() {
        int n = count(root);
        Turno[] a = new Turno[n];
        fillInorder(root, a, new IntRef(0));
        return a;
    }
    private int count(Node n) { if (n==null) return 0; return 1 + count(n.izq) + count(n.der); }
    private static class IntRef { int v; IntRef(int v) { this.v = v; } }
    private void fillInorder(Node n, Turno[] a, IntRef idx) {
        if (n == null) return;
        fillInorder(n.izq, a, idx);
        a[idx.v++] = n.t;
        fillInorder(n.der, a, idx);
    }

    // For demo
    public void printInOrder() { printRec(root); System.out.println(); }
    private void printRec(Node n) {
        if (n == null) return;
        printRec(n.izq);
        System.out.println("  " + n.t);
        printRec(n.der);
    }
}
