import java.util.List;

public interface BookIF {
    /**
     * CRUD
     */

    void insertBook(Book b);

    List<Book> getAllBooks();

    Book getBookByIsbn(int isbn);

    void updateBook(Book b);

    void deleteBook(Book b);
}
