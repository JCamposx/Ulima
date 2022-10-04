package singleton;

public class Singleton {
    static Singleton instance = null;

    private Singleton() {
        System.out.println("Instancia creada");
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public void hacerAlgo() {
        System.out.println("Haciendo algo sin crear una nueva instancia");
    }
}
