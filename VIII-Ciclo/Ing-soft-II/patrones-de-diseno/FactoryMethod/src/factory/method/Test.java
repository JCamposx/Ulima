package factory.method;

public class Test {
    public static void main(String[] args) {
        Fabrica_1 f = new FabricaConcreta();

        Soldado s1 = f.crearSoldado("Scout");
        s1.disparar();

        Soldado s22 = f.crearSoldado("Storm");
        s22.disparar();

        System.out.println();

        Fabrica_2 f2 = new FabricaConcretaStorm();
        Soldado st = f2.crearSoldado();
        st.disparar();
    }
}
