package decorator;

public abstract class HabitacionDecorator implements HabitacionIF  {
    private HabitacionIF h;

    public HabitacionDecorator() {
    }

    public HabitacionDecorator(HabitacionIF h) {
        this.h = h;
    }

    @Override
    public String decora() {
        return h.decora();
    }
}
