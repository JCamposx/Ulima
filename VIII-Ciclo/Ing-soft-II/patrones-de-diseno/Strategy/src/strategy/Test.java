package strategy;

public class Test {
    public static void main(String[] args) {
        // El contexto se crea solo una vez
        Contexto ctx = new Contexto();

        ctx.setE(new EstrategiaImpl1());
        ctx.hacerAlgo();

        ctx.setE(new EstrategiaImpl2());
        ctx.hacerAlgo();

        ctx.setE(new EstrategiaImpl3());
        ctx.hacerAlgo();
    }
}
