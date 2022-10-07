public class Test {
    public static void main(String[] args) {
        BookIF dao = new BookDAOSimple();

        System.out.println("=== GET ALL BOOKS ===");

        for (Book b : dao.getAllBooks()) {
            System.out.println(b);
        }

        System.out.println("=== INSERTING A NEW BOOK ===");

        Book newBook = new Book(10, "Ingenieria de Software II");

        dao.insertBook(newBook);

        for (Book b : dao.getAllBooks()) {
            System.out.println(b);
        }

        System.out.println("=== UPDATING A BOOK ===");

        Book updatedBook = dao.getBookByIsbn(2);
        dao.updateBook(updatedBook);

        for (Book b : dao.getAllBooks()) {
            System.out.println(b);
        }
    }
}
