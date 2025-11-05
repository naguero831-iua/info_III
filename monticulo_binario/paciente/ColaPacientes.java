package monticulo_binario.paciente;

import monticulo_binario.MinHeap;

public class ColaPacientes {
    //clase creada para manejar la cola de pacientes usando un montículo mínimo
    private MinHeap<Paciente> heap;

    public ColaPacientes() {
        heap = new MinHeap<>(5); // capacidad inicial
    }

    public void ingresar(Paciente p) {
        System.out.println("Ingresando: " + p);
        heap.add(p);
    }

    public Paciente atender() {
        Paciente p = heap.poll();
        System.out.println("Atendiendo: " + p);
        return p;
    }

    public boolean estaVacia() {
        return heap.isEmpty();
    }
}
