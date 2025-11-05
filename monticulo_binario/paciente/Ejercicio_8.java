package monticulo_binario.paciente;

public class Ejercicio_8 {
    public static void main(String[] args) {
        ColaPacientes cola = new ColaPacientes();

        cola.ingresar(new Paciente("Ana", 2));
        cola.ingresar(new Paciente("Luis", 1));
        cola.ingresar(new Paciente("Carla", 3));
        cola.ingresar(new Paciente("Pedro", 1));
        cola.ingresar(new Paciente("Sofía", 2));

        System.out.println("\n--- Orden de atención ---");
        while (!cola.estaVacia()) {
            cola.atender();
        }
    }
}
