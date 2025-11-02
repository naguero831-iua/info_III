package integrador.hash;
import integrador.model.Paciente;

/*
 Hash por encadenamiento (propio), rehash dinámico.
 No usa HashMap ni ArrayList de JDK.
*/

public class MapaPacientes {
    static class Entry {
        String key; Paciente val; Entry next;
        Entry(String k, Paciente v, Entry n) { key=k; val=v; next=n; }
    }

    private Entry[] table;
    private int size;

    public MapaPacientes(int initial) {
        table = new Entry[Math.max(3, initial)];
        size = 0;
    }

    private int hash(String s) {
        int h = 0;
        for (int i=0;i<s.length();i++) h = 31*h + s.charAt(i);
        return (h & 0x7fffffff) % table.length;
    }

    public void put(String dni, Paciente p) {
        if (containsKey(dni)) return;
        if ((double)(size+1)/table.length > 0.75) rehash();
        int idx = hash(dni);
        table[idx] = new Entry(dni, p, table[idx]);
        size++;
    }

    public Paciente get(String dni) {
        int idx = hash(dni);
        Entry e = table[idx];
        while (e!=null) {
            if (e.key.equals(dni)) return e.val;
            e = e.next;
        }
        return null;
    }

    public boolean remove(String dni) {
        int idx = hash(dni);
        Entry cur = table[idx], prev = null;
        while (cur!=null) {
            if (cur.key.equals(dni)) {
                if (prev==null) table[idx] = cur.next;
                else prev.next = cur.next;
                size--; return true;
            }
            prev = cur; cur = cur.next;
        }
        return false;
    }

    public boolean containsKey(String dni) { return get(dni) != null; }

    public int size() { return size; }

    private void rehash() {
        Entry[] old = table;
        table = new Entry[old.length*2 + 1];
        size = 0;
        for (Entry e : old) {
            while (e != null) {
                put(e.key, e.val);
                e = e.next;
            }
        }
    }

    // Iterador simple: devuelve array de claves (evita usar List)
    public String[] keysArray() {
        String[] arr = new String[size];
        int p = 0;
        for (Entry bucket : table) {
            Entry e = bucket;
            while (e != null) {
                arr[p++] = e.key;
                e = e.next;
            }
        }
        return arr;
    }
}
