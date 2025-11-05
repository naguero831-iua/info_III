package integrador.queue;

/*
 Cola circular simple que guarda DNIs (String).
*/

public class SalaEspera {
    private final String[] data;
    private int front = 0, rear = 0, size = 0, cap;

    public SalaEspera(int capacidad) {
        this.cap = Math.max(2, capacidad);
        this.data = new String[this.cap];
    }

    public synchronized void llega(String dni) {
        if (size == cap) {
            // sobrescribir el más viejo (política elegida)
            data[front] = dni;
            front = (front + 1) % cap;
            rear = front;
        } else {
            data[rear] = dni;
            rear = (rear + 1) % cap;
            size++;
        }
    }

    public synchronized String atiende() {
        if (size == 0) return null;
        String r = data[front];
        data[front] = null;
        front = (front + 1) % cap;
        size--;
        return r;
    }

    public synchronized String peek() { 
        return size==0?null:data[front]; 
    }

    public synchronized int size() { 
        return size; 
    }

    public synchronized void dump() {
        System.out.print("FRONT->[");
        for (int i=0;i<size;i++) {
            int idx = (front + i) % cap;
            System.out.print(data[idx] + (i+1==size?"":","));
        }
        System.out.println("]<-REAR");
    }
}
