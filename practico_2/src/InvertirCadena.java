public class InvertirCadena {
    public static void main(String[] args) {
        String cadena = "Hola Mundo";
        String cadenaInvertida = invertirCadena(cadena);
        System.out.println("Cadena original: " + cadena);
        System.out.println("Cadena invertida: " + cadenaInvertida);
    }

    public static String invertirCadena(String str) {
        if (str.isEmpty()) {
            return str;
        } else {
            return str.charAt(str.length() - 1) + invertirCadena(str.substring(0, str.length() - 1));
        }
    }
}
