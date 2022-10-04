package decorator;

public abstract class ArbolDecorator implements ArbolIF {
    private ArbolIF a;

    public ArbolDecorator() {
    }

    public ArbolDecorator(ArbolIF a) {
        this.a = a;
    }

    @Override
    public String decora() {
        return a.decora();
    }
}
