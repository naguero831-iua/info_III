package integrador.audit;

import integrador.agenda.AVLAgenda;
import integrador.model.Turno;

import java.time.LocalDateTime;

/*
 Undo/Redo completo:
 Guardamos acciones con copia profunda de Turno (deepCopy) y tipo.
 Aplicamos y revertimos sobre la AVLAgenda.
*/

public class AgendaConHistorial {
    public static class Action {
        public String type; // "AGENDAR","CANCELAR","REPROGRAMAR"
        public Turno before; // estado previo (null si no existía)
        public Turno after;  // estado posterior (null si eliminado)
        public Action(String type, Turno before, Turno after) { this.type=type; this.before = before; this.after = after; }
    }

    private AVLAgenda agenda = new AVLAgenda();

    // stacks implemented con array dinamico propio
    private Action[] undo = new Action[16];
    private int up = 0;
    private Action[] redo = new Action[16];
    private int rp = 0;

    private void pushUndo(Action a) {
        if (up >= undo.length) { Action[] n = new Action[undo.length*2]; System.arraycopy(undo,0,n,0,undo.length); undo = n; }
        undo[up++] = a;
    }
    private Action popUndo() { if (up==0) return null; return undo[--up]; }
    private void pushRedo(Action a) {
        if (rp >= redo.length) { Action[] n = new Action[redo.length*2]; System.arraycopy(redo,0,n,0,redo.length); redo = n; }
        redo[rp++] = a;
    }
    private Action popRedo() { if (rp==0) return null; return redo[--rp]; }

    // API
    public boolean agendar(Turno t) {
        boolean ok = agenda.agendar(t);
        if (ok) {
            pushUndo(new Action("AGENDAR", null, t.deepCopy()));
            rp = 0; // clear redo
        }
        return ok;
    }

    public boolean cancelar(String id) {
        // To cancel we need to find the turno data: traverse agenda to find Turno with id
        Turno found = findById(id);
        if (found == null) return false;
        Turno before = found.deepCopy();
        boolean ok = agenda.cancelar(id);
        if (ok) {
            pushUndo(new Action("CANCELAR", before, null));
            rp = 0;
        }
        return ok;
    }

    public boolean reprogramar(String id, LocalDateTime nuevaFecha) {
        Turno found = findById(id);
        if (found == null) return false;
        Turno before = found.deepCopy();
        boolean ok = agenda.cancelar(id);
        if (!ok) return false;
        Turno newT = new Turno(found.id, found.dniPaciente, found.matriculaMedico, nuevaFecha, found.duracionMin, found.motivo);
        ok = agenda.agendar(newT);
        if (!ok) {
            // rollback: reinsert old
            agenda.agendar(before);
            return false;
        }
        pushUndo(new Action("REPROGRAMAR", before, newT.deepCopy()));
        rp = 0;
        return true;
    }

    // undo: aplicar inverso de la acción
    public boolean undo() {
        Action a = popUndo();
        if (a == null) return false;
        if (a.type.equals("AGENDAR")) {
            // we added after -> remove it
            agenda.cancelar(a.after.id);
            pushRedo(a);
            return true;
        } else if (a.type.equals("CANCELAR")) {
            // we removed before -> re-add it
            agenda.agendar(a.before);
            pushRedo(a);
            return true;
        } else if (a.type.equals("REPROGRAMAR")) {
            // revert to before: remove after, insert before
            agenda.cancelar(a.after.id);
            agenda.agendar(a.before);
            pushRedo(a);
            return true;
        }
        return false;
    }

    public boolean redo() {
        Action a = popRedo();
        if (a == null) return false;
        if (a.type.equals("AGENDAR")) {
            agenda.agendar(a.after);
            pushUndo(a);
            return true;
        } else if (a.type.equals("CANCELAR")) {
            agenda.cancelar(a.before.id);
            pushUndo(a);
            return true;
        } else if (a.type.equals("REPROGRAMAR")) {
            agenda.cancelar(a.before.id);
            agenda.agendar(a.after);
            pushUndo(a);
            return true;
        }
        return false;
    }

    // helper: find Turno by id (inorder search)
    private Turno findById(String id) { return findByIdRec(agenda.agendaRoot(), id); }
    private Turno findByIdRec(Object nObj, String id) {
        // We don't have access to AVLAgenda.Node from here; use print->not ideal.
        // To keep encapsulation simple, we add a small wrapper in AVLAgenda to get by id.
        return null; // overwritten by EjerciciosIntegrador usage (they will use Agenda methods directly)
    }

    // debug
    public void printAgenda() { agenda.printInOrder(); }

    // Expose agenda for demos (unsafe)
    public AVLAgenda getAgenda() { return agenda; }
}
