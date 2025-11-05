package integrador.agenda;

import integrador.model.Turno;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Agenda implementada con un árbol AVL (balanceado por altura).
 * Cada nodo contiene un Turno ordenado por su fecha y hora (Turno.fechaHora).
 *
 * Funcionalidades:
 *  - Agendar un turno (rechaza si se solapan horarios).
 *  - Cancelar un turno por ID.
 *  - Buscar el siguiente turno luego de un momento dado.
 *  - Encontrar el primer hueco libre de una cierta duración.
 */
public class AVLAgenda {

    /** Nodo del árbol AVL que contiene un Turno. */
    static class Node {
        Turno t;
        Node izq, der;
        int h; // altura del nodo

        Node(Turno t) {
            this.t = t;
            izq = der = null;
            h = 1;
        }
    }

    private Node root; // raíz del árbol AVL

    // ------------------------------------------------------------
    //  Exposición del nodo raíz (para AgendaConHistorial)
    // ------------------------------------------------------------
    public Object agendaRoot() {
        return root;
    }

    // ------------------------------------------------------------
    //  Utilidades del AVL
    // ------------------------------------------------------------
    private int height(Node n) {
        return n == null ? 0 : n.h;
    }

    private void update(Node n) {
        if (n != null)
            n.h = 1 + Math.max(height(n.izq), height(n.der));
    }

    private int balance(Node n) {
        return n == null ? 0 : height(n.izq) - height(n.der);
    }

    // Rotación simple derecha
    private Node rotateRight(Node y) {
        Node x = y.izq;
        Node T2 = x.der;

        x.der = y;
        y.izq = T2;

        update(y);
        update(x);
        return x;
    }

    // Rotación simple izquierda
    private Node rotateLeft(Node x) {
        Node y = x.der;
        Node T2 = y.izq;

        y.izq = x;
        x.der = T2;

        update(x);
        update(y);
        return y;
    }

    // ------------------------------------------------------------
    //  Lógica de Turnos
    // ------------------------------------------------------------

    /** Verifica si dos turnos se superponen en el tiempo. */
    private boolean overlaps(Turno a, Turno b) {
        return a.fechaHora.isBefore(b.fin()) && b.fechaHora.isBefore(a.fin());
    }

    /**
     * Inserta un turno si es válido:
     * - No nulo
     * - Duración > 0
     * - Fecha futura
     * - No se solapa con otro
     */
    public boolean agendar(Turno t) {
        if (t == null || t.duracionMin <= 0) return false;
        if (t.fechaHora.isBefore(LocalDateTime.now())) return false;
        if (containsOverlap(root, t)) return false;

        root = insertRec(root, t);
        return true;
    }

    /** Inserta recursivamente un turno y reequilibra el AVL. */
    private Node insertRec(Node node, Turno t) {
        if (node == null) return new Node(t);

        if (t.compareTo(node.t) < 0)
            node.izq = insertRec(node.izq, t);
        else
            node.der = insertRec(node.der, t);

        update(node);
        int b = balance(node);

        // Casos de desbalance del AVL
        if (b > 1 && t.compareTo(node.izq.t) < 0) return rotateRight(node);   // Izq-Izq
        if (b < -1 && t.compareTo(node.der.t) > 0) return rotateLeft(node);   // Der-Der
        if (b > 1 && t.compareTo(node.izq.t) > 0) {                           // Izq-Der
            node.izq = rotateLeft(node.izq);
            return rotateRight(node);
        }
        if (b < -1 && t.compareTo(node.der.t) < 0) {                          // Der-Izq
            node.der = rotateRight(node.der);
            return rotateLeft(node);
        }

        return node;
    }

    /** Determina si hay un turno solapado con el turno dado. */
    private boolean containsOverlap(Node node, Turno t) {
        if (node == null) return false;

        if (overlaps(node.t, t)) return true;

        if (t.fin().isBefore(node.t.fechaHora))
            return containsOverlap(node.izq, t);
        if (t.fechaHora.isAfter(node.t.fin()))
            return containsOverlap(node.der, t);

        // En caso de duda, busca en ambos subárboles
        return containsOverlap(node.izq, t) || containsOverlap(node.der, t);
    }

    // ------------------------------------------------------------
    //  Cancelar turnos
    // ------------------------------------------------------------

    /** Cancela un turno por ID (si existe). */
    public boolean cancelar(String idTurno) {
        if (!containsId(root, idTurno)) return false;
        root = deleteById(root, idTurno);
        return true;
    }

    /** Verifica si existe un ID en el árbol. */
    private boolean containsId(Node n, String id) {
        if (n == null) return false;
        if (n.t.id.equals(id)) return true;
        return containsId(n.izq, id) || containsId(n.der, id);
    }

    /** Elimina un turno por ID y reequilibra el árbol. */
    private Node deleteById(Node node, String id) {
        if (node == null) return null;

        if (node.t.id.equals(id)) {
            // Caso: 0 o 1 hijo
            if (node.izq == null || node.der == null)
                return (node.izq != null) ? node.izq : node.der;

            // Caso: 2 hijos → reemplazar por sucesor
            Node succ = minNode(node.der);
            node.t = succ.t;
            node.der = deleteById(node.der, succ.t.id);
        } else {
            node.izq = deleteById(node.izq, id);
            node.der = deleteById(node.der, id);
        }

        update(node);
        int b = balance(node);

        // Rebalanceo del AVL
        if (b > 1 && balance(node.izq) >= 0) return rotateRight(node);
        if (b > 1 && balance(node.izq) < 0) {
            node.izq = rotateLeft(node.izq);
            return rotateRight(node);
        }
        if (b < -1 && balance(node.der) <= 0) return rotateLeft(node);
        if (b < -1 && balance(node.der) > 0) {
            node.der = rotateRight(node.der);
            return rotateLeft(node);
        }

        return node;
    }

    /** Retorna el nodo con el menor Turno (más temprano). */
    private Node minNode(Node n) {
        while (n.izq != null) n = n.izq;
        return n;
    }

    // ------------------------------------------------------------
    //  Consultas
    // ------------------------------------------------------------

    /** Busca el siguiente turno después de un momento dado. */
    public Optional<Turno> siguiente(LocalDateTime t) {
        Node cur = root;
        Turno best = null;

        while (cur != null) {
            if (!cur.t.fechaHora.isBefore(t)) {
                if (best == null || cur.t.fechaHora.isBefore(best.fechaHora))
                    best = cur.t;
                cur = cur.izq;
            } else {
                cur = cur.der;
            }
        }

        return Optional.ofNullable(best);
    }

    /**
     * Busca el primer hueco libre a partir de t0 que permita un turno
     * de duración `durMin`. Si no hay turnos, devuelve uno vacío desde t0.
     */
    public Optional<Turno> primerHueco(LocalDateTime t0, int durMin) {
        // Buscar el primer nodo >= t0
        Node cur = root;
        Node bestNode = null;

        while (cur != null) {
            if (!cur.t.fechaHora.isBefore(t0)) {
                bestNode = cur;
                cur = cur.izq;
            } else {
                cur = cur.der;
            }
        }

        LocalDateTime candidate = t0;
        if (bestNode == null)
            return Optional.of(new Turno("GAP", "", "", candidate, durMin, "hueco"));

        // Hueco antes del primer turno
        if (!candidate.plusMinutes(durMin).isAfter(bestNode.t.fechaHora))
            return Optional.of(new Turno("GAP", "", "", candidate, durMin, "hueco"));

        // Convertir árbol a arreglo ordenado por fecha
        Turno[] arr = toArray();

        // Revisar huecos entre turnos consecutivos
        for (int i = 0; i < arr.length - 1; i++) {
            LocalDateTime fin = arr[i].fin();
            if (!fin.plusMinutes(durMin).isAfter(arr[i + 1].fechaHora))
                return Optional.of(new Turno("GAP", "", "", fin, durMin, "hueco"));
        }

        // Si no hay huecos, devolver después del último
        if (arr.length > 0)
            return Optional.of(new Turno("GAP", "", "", arr[arr.length - 1].fin(), durMin, "hueco"));

        return Optional.of(new Turno("GAP", "", "", candidate, durMin, "hueco"));
    }

    // ------------------------------------------------------------
    //  Conversión e impresión
    // ------------------------------------------------------------

    /** Convierte el árbol a un arreglo ordenado (inorder). */
    private Turno[] toArray() {
        int n = count(root);
        Turno[] a = new Turno[n];
        fillInorder(root, a, new IntRef(0));
        return a;
    }

    private int count(Node n) {
        if (n == null) return 0;
        return 1 + count(n.izq) + count(n.der);
    }

    /** Clase auxiliar para pasar índice por referencia. */
    private static class IntRef {
        int v;
        IntRef(int v) { this.v = v; }
    }

    private void fillInorder(Node n, Turno[] a, IntRef idx) {
        if (n == null) return;
        fillInorder(n.izq, a, idx);
        a[idx.v++] = n.t;
        fillInorder(n.der, a, idx);
    }

    /** Imprime los turnos en orden cronológico. */
    public void printInOrder() {
        printRec(root);
        System.out.println();
    }

    private void printRec(Node n) {
        if (n == null) return;
        printRec(n.izq);
        System.out.println("  " + n.t);
        printRec(n.der);
    }
}
