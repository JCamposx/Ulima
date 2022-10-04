package adapter;

public class Test {
    public static void main(String[] args) {
        SistemaDecimalIF sd = new SistemaDecimalImpl();

        // A pesar de que el método se llame inchToFeet, en verdad estamos
        // invocando al método inchToMeters ya que la clase se encarga de
        // convertir inch a metros. Sin embargo, como estamos "siguiendo un
        // estándar", la función se debe llamar igual.
        System.out.println(sd.inchToFeet(10));
    }
}
