package decorator.habitacion;

import decorator.HabitacionDecorator;
import decorator.HabitacionIF;

public class CortinaOscura extends HabitacionDecorator {
    public CortinaOscura(HabitacionIF h) {
        super(h);
    }

    @Override
    public String decora() {
        return super.decora() + " que tenga cortinas oscuras";
    }
}
