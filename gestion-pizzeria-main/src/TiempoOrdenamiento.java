//clase para medir el tiempo de ordenamiento
public class TiempoOrdenamiento {
    private long inicio;
    private long fin;
    public void iniciar() {
        inicio = System.nanoTime();
    }
    public void finalizar() {
        fin = System.nanoTime();
    }
    public long obtenerTiempo() {
        return fin - inicio;
    }

    public double obtenerTiempoEnMilisegundos() {
        return (fin - inicio) / 1_000_000.0;
    }
}
