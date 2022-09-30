package bridge;

public class Test {
    public static void main(String[] args) {
        ImplementorIF imp1 = new ConcreteImpl1();
        Abstraction a1 = new ConcreteAbstractionImpl1(imp1);

        a1.ejecutar();

        ImplementorIF imp2 = new ConcreteImpl2();
        Abstraction a2 = new ConcreteAbstractionImpl1(imp2);

        a2.ejecutar();
    }
}
