package ejemplo.estrategia;

public class Resta implements EstrategiaIF {
    @Override
    public int calcula(int a, int b) {
        return a - b;
    }
}
