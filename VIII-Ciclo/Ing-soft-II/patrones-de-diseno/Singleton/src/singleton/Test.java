package singleton;

public class Test {
    public static void main(String[] args) {
        DarthVader d = DarthVader.getInstance();
        d.matarGente();

        System.out.println();

        DarthVader d1 = DarthVader.getInstance();
        d1.matarGente();
    }
}
