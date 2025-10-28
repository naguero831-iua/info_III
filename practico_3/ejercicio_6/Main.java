package ejercicio_6;

public class Main {
    public static void main(String[] args) {
        DeshacerRehacer dr = new DeshacerRehacer(10);

        dr.realizarAccion(1);
        dr.realizarAccion(2);
        dr.realizarAccion(3);
        dr.mostrarEstado();

        dr.deshacer();
        dr.mostrarEstado();

        dr.rehacer();
        dr.mostrarEstado();

        dr.deshacer();
        dr.deshacer();
        dr.mostrarEstado();

        dr.rehacer();
        dr.mostrarEstado();
    }
}
