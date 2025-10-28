/*  Implemente un programa que utilice ColaArreglo para simular una fila de espera en un
banco.
● Los clientes llegan en el orden: Ana, Luis, Marta, Pedro.
● Se atienden los dos primeros clientes.
📌 Mostrar la cola antes y después de atender. */

package ejercicio_4;

public class Main {

    public static void main(String[] args) {
        ColaArreglo queue = new ColaArreglo(4);
        queue.enqueue("Ana");
        queue.enqueue("Luis");
        queue.enqueue("Marta");
        queue.enqueue("Pedro");

        System.out.println("Cola antes de atender:");
        queue.mostrarCola();
        queue.dequeue();
        queue.dequeue();
        System.out.println("Cola después de atender a dos clientes:");
        queue.mostrarCola();
    }
}
