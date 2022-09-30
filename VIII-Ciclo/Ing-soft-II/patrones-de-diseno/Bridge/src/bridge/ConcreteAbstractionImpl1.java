package bridge;

public class ConcreteAbstractionImpl1 extends Abstraction {
    public ConcreteAbstractionImpl1(ImplementorIF r) {
        super(r);
    }

    @Override
    public void ejecutar() {
        r.hacerAlgo();
    }
}
