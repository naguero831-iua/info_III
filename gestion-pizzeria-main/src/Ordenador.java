//implementare los distintos algoritmos de ordenamiento

import java.util.List;

public class Ordenador {
    public void ordenarPorNombre(List<Pedido> pedidos) {
        pedidos.sort((p1, p2) -> p1.getCliente().compareTo(p2.getCliente()));
    }
    public void ordenarPorDireccion(List<Pedido> pedidos) {
        pedidos.sort((p1, p2) -> p1.getDireccion().compareTo(p2.getDireccion()));
    }
    public void ordenarPorCantidadPizzas(List<Pedido> pedidos) {
        pedidos.sort((p1, p2) -> Integer.compare(p1.getPizzas().length, p2.getPizzas().length));
    }
    
}
