package ejemplo.estrategia;

public class Division implements EstrategiaIF {

    @Override
    public int calcula(int a, int b) {
        return a / b;
    }
}
