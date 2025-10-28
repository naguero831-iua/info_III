public class Palindromo {
    public static void main(String[] args) {
        String[] ejemplos = {"neuquen", "informatica", "anita lava la tina"};
        for (String cadena : ejemplos) {
            boolean esPalindromo = esPalindromo(cadena);
            System.out.println("La cadena \"" + cadena + "\" ----> " + esPalindromo);
        }
    }

    public static boolean esPalindromo(String str) {
        str = str.replaceAll("\\s+", "").toLowerCase(); // Eliminar espacios y convertir a minúsculas
        return esPalindromoRecursivo(str, 0, str.length() - 1);
    }

    private static boolean esPalindromoRecursivo(String str, int inicio, int fin) {
        if (inicio >= fin) {
            return true;
        }
        if (str.charAt(inicio) != str.charAt(fin)) {
            return false;
        }
        return esPalindromoRecursivo(str, inicio + 1, fin - 1);
    }
}
