package decorator;

import decorator.habitacion.Alfombra;
import decorator.habitacion.Color;
import decorator.habitacion.TV;

public class Test {
    public static void main(String[] args) {
        HabitacionIF h = new Alfombra(new TV(new Color(new HabitacionImpl(), "blanco")));
        System.out.println(h.decora());
    }
}
