package builder;

public class Soldado {
    private String serialNumber;
    private String botas;
    private String casco;
    private String arma;

    private Soldado(Builder builder) {
        this.serialNumber = builder.serialNumber;
        this.botas = builder.botas;
        this.casco = builder.casco;
        this.arma = builder.arma;
    }

    @Override
    public String toString() {
        return "Soldado " + this.serialNumber +
                "\n- Botas: " + this.botas +
                "\n- Casco: " + this.casco +
                "\n- Arma: " + this.arma;
    }

    public static class Builder {
        private String serialNumber;
        private String botas;
        private String casco;
        private String arma;

        public Builder (String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public Builder botas(String botas) {
            this.botas = botas;
            return this;
        }

        public Builder casco(String casco) {
            this.casco = casco;
            return this;
        }

        public Builder arma(String arma) {
            this.arma = arma;
            return this;
        }

        public Soldado build() {
            return new Soldado(this);
        }
    }
}
