package ejemplo.estrategia;

public class Suma implements EstrategiaIF {
    @Override
    public int calcula(int a, int b) {
        return a + b;
    }
}
