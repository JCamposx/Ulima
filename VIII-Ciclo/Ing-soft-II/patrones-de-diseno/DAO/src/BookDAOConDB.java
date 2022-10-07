import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAOConDB implements BookIF {

    public BookDAOConDB() {
    }

    @Override
    public void insertBook(Book b) {
        // this.listBooks.add(b);
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> listBooks = new ArrayList<>();

        String url = "jdbc:mysql://localhost:3306/mydb?user=admin&password=password";

        try {
            Connection conn = DriverManager.getConnection(url);

            String sql = "SELECT * FROM BOOKS ORDER BY 1";

            Statement statement = conn.createStatement();

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                Book b = new Book(rs.getInt(1), rs.getString(2));
                listBooks.add(b);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // conn.close();
            // statement.close();
            // rs.close();
        }

        return listBooks;
    }

    @Override
    public Book getBookByIsbn(int isbn) {
        String url = "jdbc:mysql://localhost:3306/mydb?user=admin&password=password";

        try {
            Connection conn = DriverManager.getConnection(url);

            String sql = "SELECT * FROM BOOKS WHERE isbn = " + isbn + " ORDER BY 1";

            Statement statement = conn.createStatement();

            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                return new Book(rs.getInt(1), rs.getString(2));
            } else {
                return new Book();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateBook(Book b) {
        // this.listBooks.
    }

    @Override
    public void deleteBook(Book b) {
        // this.listBooks.remove(b);
    }
}
