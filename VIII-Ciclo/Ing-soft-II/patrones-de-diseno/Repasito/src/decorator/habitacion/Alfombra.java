package decorator.habitacion;

import decorator.HabitacionDecorator;
import decorator.HabitacionIF;

public class Alfombra extends HabitacionDecorator {
    public Alfombra(HabitacionIF h) {
        super(h);
    }

    @Override
    public String decora() {
        return super.decora() + " que sea alfombrada";
    }
}
