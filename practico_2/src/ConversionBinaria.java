public class ConversionBinaria {
    public static void main(String[] args) {
        int numero = 13;
        String binario = convertirABinario(numero);
        System.out.println("El número " + numero + " en binario es: " + binario);
    }

    public static String convertirABinario(int n) {
        if (n == 0) {
            return "0";
        } else {
            return convertirABinario(n / 2) + (n % 2);
        }
    }
}
