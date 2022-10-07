import java.util.ArrayList;
import java.util.List;

public class BookDAOSimple implements BookIF {
    List<Book> listBooks = new ArrayList<>();

    public BookDAOSimple() {
        this.listBooks.add(new Book(1, "Algoritmos"));
        this.listBooks.add(new Book(2, "Fundamentos de programacion"));
        this.listBooks.add(new Book(3, "Programacion Orientada a Objetos"));
        this.listBooks.add(new Book(4, "Lenguajes de programacion"));
        this.listBooks.add(new Book(5, "Programacion Web"));
    }

    @Override
    public void insertBook(Book b) {
        this.listBooks.add(b);
    }

    @Override
    public List<Book> getAllBooks() {
        return this.listBooks;
    }

    @Override
    public Book getBookByIsbn(int isbn) {
        return this.listBooks.get(isbn - 1);
    }

    @Override
    public void updateBook(Book b) {
        this.listBooks.set(this.listBooks.indexOf(b), b);
    }

    @Override
    public void deleteBook(Book b) {
        this.listBooks.remove(b);
    }
}
