package strategy;

public class Contexto {
    EstrategiaIF e;

    public void setE(EstrategiaIF e) {
        this.e = e;
    }

    public void hacerAlgo() {
        e.ejecutar();
    }
}
