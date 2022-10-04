package decorator;

public class Test {
    public static void main(String[] args) {
        ArbolIF a1 = new Luz(new ArbolImpl());
        System.out.println(a1.decora());

        ArbolIF a2 = new Luz(new Figura(new ArbolImpl()));
        System.out.println(a2.decora());
    }
}
