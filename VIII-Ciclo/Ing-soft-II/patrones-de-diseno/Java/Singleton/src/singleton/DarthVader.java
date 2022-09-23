package singleton;

public class DarthVader {
    static DarthVader instance = null;

    private DarthVader() {
        System.out.println("Instancia creada: Yo soy tu padre!");
    }

    public static DarthVader getInstance() {
        if (instance == null) {
            instance = new DarthVader();
        }
        return instance;
    }

    public void matarGente() {
        System.out.println("Muere!");
    }
}
