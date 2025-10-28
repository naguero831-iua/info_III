//punto de entrada de la aplicacion
import java.util.List;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Pizzeria pizzeria = new Pizzeria();
        Ordenador ordenador = new Ordenador();
        TiempoOrdenamiento tiempo = new TiempoOrdenamiento();

        // Crear algunos pedidos de ejemplo
        pizzeria.agregarPedido(new Pedido("Juan", "Calle 123", new String[]{"Margarita", "Pepperoni"}));
        pizzeria.agregarPedido(new Pedido("Ana", "Avenida 456", new String[]{"Hawaiana"}));
        pizzeria.agregarPedido(new Pedido("Luis", "Boulevard 789", new String[]{"Cuatro Quesos", "Vegetariana", "Barbacoa"}));

        List<Pedido> pedidos = pizzeria.getPedidos();

        // Ordenar por nombre del cliente
        tiempo.iniciar();
        ordenador.ordenarPorNombre(pedidos);
        tiempo.finalizar();
        System.out.println("Pedidos ordenados por nombre:");
        imprimirPedidos(pedidos);
        System.out.println("Tiempo de ordenamiento: " + tiempo.obtenerTiempoEnMilisegundos() + " ms\n");

        // Ordenar por direccion
        tiempo.iniciar();
        ordenador.ordenarPorDireccion(pedidos);
        tiempo.finalizar();
        System.out.println("Pedidos ordenados por direccion:");
        imprimirPedidos(pedidos);
        System.out.println("Tiempo de ordenamiento: " + tiempo.obtenerTiempoEnMilisegundos() + " ms\n");

        // Ordenar por cantidad de pizzas
        tiempo.iniciar();
        ordenador.ordenarPorCantidadPizzas(pedidos);
        tiempo.finalizar();
        System.out.println("Pedidos ordenados por cantidad de pizzas:");
        imprimirPedidos(pedidos);
        System.out.println("Tiempo de ordenamiento: " + tiempo.obtenerTiempoEnMilisegundos() + " ms\n");
    }

    private static void imprimirPedidos(List<Pedido> pedidos) {
        for (Pedido pedido : pedidos) {
            System.out.println("Cliente: " + pedido.getCliente() + ", Direccion: " + pedido.getDireccion() + ", Pizzas: " + Arrays.toString(pedido.getPizzas()));
        }
    }
}
