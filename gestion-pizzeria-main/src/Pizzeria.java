//gestiona los pedidos de la pizzeria

import java.util.ArrayList;
import java.util.List;

public class Pizzeria {
    private List<Pedido> pedidos;

    public Pizzeria() {
        pedidos = new ArrayList<>();
    }

    public void agregarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }
}
