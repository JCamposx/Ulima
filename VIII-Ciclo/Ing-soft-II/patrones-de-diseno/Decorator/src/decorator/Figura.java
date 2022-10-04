package decorator;

public class Figura extends ArbolDecorator {
    public Figura() {
    }

    public Figura(ArbolIF a) {
        super(a);
    }

    @Override
    public String decora() {
        return super.decora() + " con figuritas de navidad";
    }
}
