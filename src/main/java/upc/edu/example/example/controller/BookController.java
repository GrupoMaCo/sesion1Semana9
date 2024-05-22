package upc.edu.example.example.controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.edu.example.example.exception.ValidationException;
import upc.edu.example.example.model.Book;
import upc.edu.example.example.repository.BookRepository;
import upc.edu.example.example.services.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/library")
public class BookController {
    @Autowired
    private BookService bookService;
    private final BookRepository bookRepository;
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    //Get Method : 200 status code is returned when a book is found
    //URL : http://localhost:8080/api/v1/library/books
    @Transactional (readOnly = true)
    @GetMapping ("/books")
    public ResponseEntity <List<Book>> getBooks(){
        return new ResponseEntity<List<Book>>(bookRepository.findAll(), HttpStatus.OK);
    }

    //Get Method: filterByEditorial : 200 status code is returned when a book is found
    //URL : http://localhost:8080/api/v1/library/books/filterByEditorial
    @Transactional (readOnly = true)
    @GetMapping ("/books/filterByEditorial")
    public ResponseEntity <List<Book>> getBooksByEditorial(@RequestParam String editorial){
        return new ResponseEntity<List<Book>>(bookRepository.findByEditorial(editorial), HttpStatus.OK);
    }

    //Post Method : 201 status code is returned when a new book is created
    //URL : http://localhost:8080/api/v1/library/books
    @Transactional
    @PostMapping("/books")
    public ResponseEntity <Book> createBook(@RequestBody Book book){
        existsBookByTitleAndEditorial(book);
        validateBook(book);
        return new ResponseEntity<Book>(bookService.save(book), HttpStatus.CREATED);
    }

    private void validateBook (Book book){

        if(book.getTitle()==null || book.getTitle().trim().isEmpty()){
            throw new ValidationException("The title of the book must be mandatory");
        }

        if(book.getTitle().length()>22){
            throw new ValidationException("The title of the book must not exceed 22 characters");
        }

        if (book.getEditorial()==null || book.getEditorial().trim().isEmpty()){
            throw new ValidationException("The editorial of the book must be mandatory");
        }

        if(book.getEditorial().length()>14){
            throw new ValidationException("The editorial of the book must not exceed 22 characters");
        }
    }

    private void existsBookByTitleAndEditorial (Book book){
        if(bookRepository.existsByTitleAndEditorial(book.getTitle(), book.getEditorial())){
            throw new ValidationException("The book already exists");
        }
    }

}
