//clase que representa un pedido de pizza
public class Pedido {
    private String cliente;
    private String direccion;
    private String[] pizzas;

    public Pedido(String cliente, String direccion, String[] pizzas) {
        this.cliente = cliente;
        this.direccion = direccion;
        this.pizzas = pizzas;
    }

    // Getters y Setters
    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String[] getPizzas() {
        return pizzas;
    }

    public void setPizzas(String[] pizzas) {
        this.pizzas = pizzas;
    }
}