package decorator;

public class Luz extends ArbolDecorator {
    public Luz() {
    }

    public Luz(ArbolIF a) {
        super(a);
    }

    @Override
    public String decora() {
        return super.decora() + " con luces de colores";
    }
}
