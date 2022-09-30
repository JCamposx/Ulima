package ejemplo.estrategia;

public class Multiplicacion implements EstrategiaIF {
    @Override
    public int calcula(int a, int b) {
        return a * b;
    }
}
