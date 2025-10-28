public class BuscarElemArray {
    public static void main(String[] args) {
        int[] array = {1, 2, 3, 4, 5};
        int elementoABuscar = 8;
        boolean encontrado = buscarElemento(array, array.length, elementoABuscar);
        if (encontrado) {
            System.out.println("El elemento " + elementoABuscar + " se encuentra en el array.");
        } else {
            System.out.println("El elemento " + elementoABuscar + " no se encuentra en el array.");
        }
    }

    public static boolean buscarElemento(int[] arr, int n, int elemento) {
        if (n <= 0) {
            return false;
        } else if (arr[n - 1] == elemento) {
            return true;
        } else {
            return buscarElemento(arr, n - 1, elemento);
        }
    }
}
