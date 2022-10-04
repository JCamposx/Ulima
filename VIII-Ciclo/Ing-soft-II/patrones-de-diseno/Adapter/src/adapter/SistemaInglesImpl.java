package adapter;

public class SistemaInglesImpl implements SistemaInglesIF {
    @Override
    public float inchToFeet(float inch) {
        return inch / 12;
    }
}
