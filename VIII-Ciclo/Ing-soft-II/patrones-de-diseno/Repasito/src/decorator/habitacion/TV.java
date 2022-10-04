package decorator.habitacion;

import decorator.HabitacionDecorator;
import decorator.HabitacionIF;

public class TV extends HabitacionDecorator {
    public TV(HabitacionIF h) {
        super(h);
    }

    @Override
    public String decora() {
        return super.decora() + " que tenga TV";
    }
}
