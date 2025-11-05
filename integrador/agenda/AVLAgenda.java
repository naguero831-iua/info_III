package integrador.agenda;

import integrador.model.Turno;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementación de una agenda de turnos médicos utilizando un Árbol AVL.
 * El árbol se mantiene ordenado por la fecha y hora de los turnos.
 *
 * Funcionalidades principales:
 * - Agendar un turno, evitando solapamientos.
 * - Cancelar un turno por su ID.
 * - Encontrar el siguiente turno disponible a partir de una fecha.
 * - Buscar el primer hueco libre para un nuevo turno.
 */
public class AVLAgenda {

    /**
     * Nodo interno del Árbol AVL.
     * Contiene el turno, referencias a los hijos izquierdo y derecho, y la altura del subárbol.
     */
    static class Node {
        Turno t;       // El turno almacenado en el nodo.
        Node izq, der; // Punteros a los hijos izquierdo y derecho.
        int h;         // Altura del subárbol que tiene este nodo como raíz.

        Node(Turno t) {
            this.t = t;
            izq = der = null;
            h = 1; // Un nodo nuevo siempre tiene altura 1.
        }
    }

    private Node root; // Raíz del Árbol AVL.

    /**
     * Expone la raíz del árbol de forma segura (como Object) para ser utilizada
     * por otras clases que necesiten acceder a la estructura interna, como AgendaConHistorial.
     */
    public Object agendaRoot() {
        return root;
    }

    // --- Métodos auxiliares del Árbol AVL ---

    /** Retorna la altura de un nodo (0 si es nulo). */
    private int height(Node n) {
        return n == null ? 0 : n.h;
    }

    /** Actualiza la altura de un nodo basándose en la altura de sus hijos. */
    private void upd(Node n) {
        if (n != null) {
            n.h = 1 + Math.max(height(n.izq), height(n.der));
        }
    }

    /** Calcula el factor de balance de un nodo (diferencia de altura entre subárboles). */
    private int balance(Node n) {
        return n == null ? 0 : height(n.izq) - height(n.der);
    }

    /** Realiza una rotación simple a la derecha. */
    private Node rotRight(Node y) {
        Node x = y.izq;
        Node T2 = x.der;
        x.der = y;
        y.izq = T2;
        upd(y); // Actualiza altura del nodo movido.
        upd(x); // Actualiza altura del nuevo nodo raíz.
        return x;
    }

    /** Realiza una rotación simple a la izquierda. */
    private Node rotLeft(Node x) {
        Node y = x.der;
        Node T2 = y.izq;
        y.izq = x;
        x.der = T2;
        upd(x); // Actualiza altura.
        upd(y); // Actualiza altura.
        return y;
    }

    /**
     * Verifica si dos turnos se solapan en el tiempo.
     * Dos turnos se solapan si el inicio de uno es antes del fin del otro, y viceversa.
     */
    private boolean overlaps(Turno a, Turno b) {
        return a.fechaHora.isBefore(b.fin()) && b.fechaHora.isBefore(a.fin());
    }

    // --- API Pública de la Agenda ---

    /**
     * Agenda un nuevo turno.
     * Rechaza el turno si es nulo, su duración es inválida, es en el pasado, o se solapa con otro existente.
     */
    public boolean agendar(Turno t) {
        if (t == null || t.duracionMin <= 0) return false;
        if (t.fechaHora.isBefore(LocalDateTime.now())) return false; // No se pueden agendar turnos en el pasado.
        if (containsOverlap(root, t)) return false; // Evita solapamientos.
        root = insertRec(root, t); // Inserta en el árbol.
        return true;
    }

    /**
     * Método recursivo para insertar un turno en el árbol y mantenerlo balanceado.
     */
    private Node insertRec(Node node, Turno t) {
        // 1. Inserción estándar de un BST.
        if (node == null) return new Node(t);

        if (t.compareTo(node.t) < 0) {
            node.izq = insertRec(node.izq, t);
        } else {
            node.der = insertRec(node.der, t);
        }

        // 2. Actualizar altura del nodo actual.
        upd(node);

        // 3. Obtener el factor de balance y aplicar rotaciones si es necesario.
        int b = balance(node);

        // Caso Izquierda-Izquierda
        if (b > 1 && t.compareTo(node.izq.t) < 0) return rotRight(node);
        // Caso Derecha-Derecha
        if (b < -1 && t.compareTo(node.der.t) > 0) return rotLeft(node);
        // Caso Izquierda-Derecha
        if (b > 1 && t.compareTo(node.izq.t) > 0) {
            node.izq = rotLeft(node.izq);
            return rotRight(node);
        }
        // Caso Derecha-Izquierda
        if (b < -1 && t.compareTo(node.der.t) < 0) {
            node.der = rotRight(node.der);
            return rotLeft(node);
        }

        return node; // Retorna el nodo sin cambios si está balanceado.
    }

    /**
     * Verifica de forma recursiva si un nuevo turno `t` se solapa con algún turno en el árbol.
     * Optimiza la búsqueda aprovechando el orden del árbol.
     */
    private boolean containsOverlap(Node node, Turno t) {
        if (node == null) return false;

        if (overlaps(node.t, t)) return true; // Hay solapamiento con el nodo actual.

        // Si el nuevo turno termina antes de que empiece el turno actual, solo busca en la izquierda.
        if (t.fin().isBefore(node.t.fechaHora)) {
            return containsOverlap(node.izq, t);
        }
        // Si el nuevo turno empieza después de que termine el turno actual, solo busca en la derecha.
        if (t.fechaHora.isAfter(node.t.fin())) {
            return containsOverlap(node.der, t);
        }

        // Si no, el rango del nuevo turno podría solaparse con ambos subárboles.
        return containsOverlap(node.izq, t) || containsOverlap(node.der, t);
    }

    /**
     * Cancela un turno buscándolo por su ID.
     */
    public boolean cancelar(String idTurno) {
        if (!containsId(root, idTurno)) return false; // El turno no existe.
        root = deleteById(root, idTurno);
        return true;
    }

    /**
     * Búsqueda simple para verificar si un ID de turno existe en el árbol.
     * Recorre todo el árbol si es necesario.
     */
    private boolean containsId(Node n, String id) {
        if (n == null) return false;
        if (n.t.id.equals(id)) return true;
        return containsId(n.izq, id) || containsId(n.der, id);
    }

    /**
     * Método recursivo para eliminar un nodo por ID y rebalancear el árbol.
     */
    private Node deleteById(Node node, String id) {
        if (node == null) return null;

        // Búsqueda del nodo a eliminar. Como no está ordenado por ID, debemos buscar en ambos subárboles.
        if (node.t.id.equals(id)) {
            // Nodo encontrado. Procedemos a eliminarlo.
            if (node.izq == null || node.der == null) {
                // Caso con 0 o 1 hijo.
                return node.izq != null ? node.izq : node.der;
            } else {
                // Caso con 2 hijos.
                Node succ = minNode(node.der); // Encontrar el sucesor in-order.
                node.t = succ.t; // Reemplazar los datos del nodo.
                node.der = deleteById(node.der, succ.t.id); // Eliminar el sucesor.
            }
        } else {
            // Continuar la búsqueda en los subárboles.
            node.izq = deleteById(node.izq, id);
            node.der = deleteById(node.der, id);
        }

        // Actualizar altura y rebalancear.
        upd(node);
        int b = balance(node);

        if (b > 1 && balance(node.izq) >= 0) return rotRight(node);
        if (b > 1 && balance(node.izq) < 0) {
            node.izq = rotLeft(node.izq);
            return rotRight(node);
        }
        if (b < -1 && balance(node.der) <= 0) return rotLeft(node);
        if (b < -1 && balance(node.der) > 0) {
            node.der = rotRight(node.der);
            return rotLeft(node);
        }

        return node;
    }

    /** Encuentra el nodo con el valor mínimo en un subárbol (el que está más a la izquierda). */
    private Node minNode(Node n) {
        while (n.izq != null) n = n.izq;
        return n;
    }

    /**
     * Encuentra el próximo turno agendado a partir de una fecha y hora `t`.
     */
    public Optional<Turno> siguiente(LocalDateTime t) {
        Node cur = root;
        Turno best = null;
        while (cur != null) {
            // Si el turno actual no es anterior a `t`.
            if (!cur.t.fechaHora.isBefore(t)) {
                // Es un candidato. Guardamos el mejor que hemos encontrado hasta ahora.
                if (best == null || cur.t.fechaHora.isBefore(best.fechaHora)) {
                    best = cur.t;
                }
                cur = cur.izq; // Intentamos encontrar uno aún más cercano en el subárbol izquierdo.
            } else {
                cur = cur.der; // El turno actual es demasiado temprano, buscamos en la derecha.
            }
        }
        return Optional.ofNullable(best);
    }

    /**
     * Encuentra el primer hueco disponible de `durMin` minutos a partir de `t0`.
     * Este método es una simplificación y podría ser ineficiente en agendas muy densas.
     */
    public Optional<Turno> primerHueco(LocalDateTime t0, int durMin) {
        // find first >= t0
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
        // Convertimos el árbol a un array ordenado para facilitar la búsqueda de huecos.
        Turno[] buffer = toArray();

        // 1. Revisar si hay espacio antes del primer turno.
        LocalDateTime candidate = t0;
        if (buffer.length == 0 || !candidate.plusMinutes(durMin).isAfter(buffer[0].fechaHora)) {
            return Optional.of(new Turno("GAP", "", "", candidate, durMin, "hueco"));
        }

        // 2. Revisar los huecos entre turnos consecutivos.
        for (int i = 0; i < buffer.length - 1; i++) {
            LocalDateTime end = buffer[i].fin();
            // Si el fin del turno `i` más la duración requerida no se solapa con el inicio del turno `i+1`.
            if (!end.plusMinutes(durMin).isAfter(buffer[i + 1].fechaHora)) {
                return Optional.of(new Turno("GAP", "", "", end, durMin, "hueco"));
            }
        }

        // 3. Si no hay huecos intermedios, el primer hueco está después del último turno.
        if (buffer.length > 0) {
            return Optional.of(new Turno("GAP", "", "", buffer[buffer.length - 1].fin(), durMin, "hueco"));
        }

        return Optional.of(new Turno("GAP", "", "", candidate, durMin, "hueco")); // Si la agenda está vacía.
    }

    // --- Métodos de Conversión y Depuración ---

    /** Convierte el árbol a un array de Turnos ordenado por fecha. */
    private Turno[] toArray() {
        int n = count(root);
        Turno[] a = new Turno[n];
        fillInorder(root, a, new IntRef(0));
        return a;
    }

    /** Cuenta el número de nodos en el árbol. */
    private int count(Node n) {
        if (n == null) return 0;
        return 1 + count(n.izq) + count(n.der);
    }

    /** Clase auxiliar para pasar un índice por referencia en la recursión. */
    private static class IntRef {
        int v;
        IntRef(int v) {
            this.v = v;
        }
    }

    /** Rellena un array con los turnos del árbol en orden (in-order traversal). */
    private void fillInorder(Node n, Turno[] a, IntRef idx) {
        if (n == null) return;
        fillInorder(n.izq, a, idx);
        a[idx.v++] = n.t;
        fillInorder(n.der, a, idx);
    }

    /**
     * Imprime todos los turnos de la agenda en orden para depuración.
     */
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
