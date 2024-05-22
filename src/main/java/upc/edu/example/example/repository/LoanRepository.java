package upc.edu.example.example.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import upc.edu.example.example.model.Loan;
import upc.edu.example.example.model.Book;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long>{
    List<Loan> findByCodeStudent(String codeStudent);

    boolean existsByCodeStudentAndBookAndBookLoan(String codeStudent, Book book, boolean bookLoan);
}
