import java.io.Serializable;

public class Book implements Serializable {
    private int isbn;
    private String nombre;

    public Book() {
    }

    public Book(int isbn, String nombre) {
        this.isbn = isbn;
        this.nombre = nombre;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Book " + this.isbn + " -> " + this.nombre;
    }
}
