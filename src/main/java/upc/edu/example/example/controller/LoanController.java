package upc.edu.example.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import upc.edu.example.example.exception.ValidationException;
import upc.edu.example.example.model.Loan;
import upc.edu.example.example.model.Book;
import upc.edu.example.example.repository.BookRepository;
import upc.edu.example.example.repository.LoanRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/loans")
public class LoanController {
    private LoanRepository loanRepository;
    private BookRepository bookRepository;

    public LoanController(LoanRepository loanRepository, BookRepository bookRepository) {

        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

    //Method: GET
    //URL: http://localhost:8080/api/v1/loans/filterByCodeStudent
    @Transactional(readOnly = true)
    @GetMapping("/filterByCodeStudent")
    public ResponseEntity<List<Loan>> getLoansByCodeStudent(@RequestParam(value = "codeStudent") String codeStudent) {
        return new ResponseEntity<List<Loan>>(loanRepository.findByCodeStudent(codeStudent), HttpStatus.OK);
    }

    //Method: Post

//URL: localhost:8080/api/v1/loans/books/1

    @Transactional

    @PostMapping("/books/{id}")

    public ResponseEntity<Loan> createLoan(@PathVariable(name = "id")Long bookId, @RequestBody Loan loan){

        Book book= bookRepository.findById(bookId).orElseThrow(()->new ValidationException("The book with the id="+bookId+" was not found"));

        existsLoanByCodeStudentAndBookAndDevolution(loan, book);
        validateLoan(loan);
        loan.setLoanDate(LocalDate.now());
        loan.setDevolutionDate(loan.getLoanDate().plusDays(3));
        loan.setBookLoan(true);

        return new ResponseEntity<Loan>(loanRepository.save(loan), HttpStatus.CREATED);

    }

    private void validateLoan(Loan loan){
        if(loan.getCodeStudent()==null || loan.getCodeStudent().trim().isEmpty()){
            throw new ValidationException("the code of the student must be mandatory");
        }

        if(loan.getCodeStudent().length()<10){
            throw new ValidationException("the code of the student must have 10 characters");
        }
    }

    private void existsLoanByCodeStudentAndBookAndDevolution(Loan loan, Book book){
        if(loanRepository.existsByCodeStudentAndBookAndBookLoan(loan.getCodeStudent(), book, false)){
            throw new ValidationException("The student has not returned the book");
    }
    }

}

