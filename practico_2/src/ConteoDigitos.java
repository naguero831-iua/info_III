public class ConteoDigitos {

    public static void main(String[] args) {
        int numero = 8977896;
        int cantidadDigitos = contarDigitos(numero);
        System.out.println("El número " + numero + " tiene " + cantidadDigitos + " dígitos.");
    }


    public static int contarDigitos(int n) {
        if (n < 10) {
            return 1;
        } else {
            return 1 + contarDigitos(n / 10);
        }
    }
    
}
