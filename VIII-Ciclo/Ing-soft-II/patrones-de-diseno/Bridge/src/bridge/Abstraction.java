package bridge;

public abstract class Abstraction {
    protected ImplementorIF r;

    public Abstraction(ImplementorIF r) {
        this.r = r;
    }

    public abstract void ejecutar();
}
