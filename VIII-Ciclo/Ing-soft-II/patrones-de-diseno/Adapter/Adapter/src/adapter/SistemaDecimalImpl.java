package adapter;

public class SistemaDecimalImpl implements SistemaDecimalIF {
    @Override
    public float inchToFeet(float inch) {
        return inchToMeters(inch);
    }

    @Override
    // A pesar de estar público, debería estar privado ya que no se debe de poder
    // invocar desde otro lado. Solo que Java no deja ponerlo en private.
    public float inchToMeters(float inch) {
        return inch * 0.0254f;
    }
}
