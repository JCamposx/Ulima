package builder;

public class Soldado {
    private String serialNumber;
    private String casco;
    private String armadura;
    private String guantes;
    private String botas;
    private String arma;

    /**
     * En builder, el constructor debe ser privado
     */
    private Soldado(Builder builder) {
        this.serialNumber = builder.serialNumber;
        this.casco = builder.casco;
        this.armadura = builder.armadura;
        this.guantes = builder.guantes;
        this.botas = builder.botas;
        this.arma = builder.arma;
    }

    public void disparar() {
        System.out.println("Pium pium ...");
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCasco() {
        return casco;
    }

    public void setCasco(String casco) {
        this.casco = casco;
    }

    public String getArmadura() {
        return armadura;
    }

    public void setArmadura(String armadura) {
        this.armadura = armadura;
    }

    public String getGuantes() {
        return guantes;
    }

    public void setGuantes(String guantes) {
        this.guantes = guantes;
    }

    public String getBotas() {
        return botas;
    }

    public void setBotas(String botas) {
        this.botas = botas;
    }

    public String getArma() {
        return arma;
    }

    public void setArma(String arma) {
        this.arma = arma;
    }

    @Override
    public String toString() {
        return "Soldado{" +
                "serialNumber='" + serialNumber + '\'' +
                ", casco='" + casco + '\'' +
                ", armadura='" + armadura + '\'' +
                ", guantes='" + guantes + '\'' +
                ", botas='" + botas + '\'' +
                ", arma='" + arma + '\'' +
                '}';
    }

    // Inner class
    public static class Builder {
        private String serialNumber;
        private String casco;
        private String armadura;
        private String guantes;
        private String botas;
        private String arma;

        /**
         * En en constructor de builder deben de ir los atributos obligatorios
         */
        public Builder(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public Builder casco(String c) {
            this.casco = c;
            return this;
        }

        public Builder armadura(String a) {
            this.armadura = a;
            return this;
        }

        public Builder guantes(String g) {
            this.guantes = g;
            return this;
        }

        public Builder botas(String b) {
            this.botas = b;
            return this;
        }

        public Builder arma(String a) {
            this.arma = a;
            return this;
        }

        public Soldado build() {
            return new Soldado(this);
        }
    } // Fin de la inner class
} // Fin de la clase soldado
