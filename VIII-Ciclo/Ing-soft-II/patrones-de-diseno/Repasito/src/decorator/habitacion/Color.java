package decorator.habitacion;

import decorator.HabitacionDecorator;
import decorator.HabitacionIF;

public class Color extends HabitacionDecorator {
    String color;

    public Color(HabitacionIF h, String color) {
        super(h);
        this.color = color;
    }

    @Override
    public String decora() {
        return super.decora() + " de color " + this.color;
    }
}
