package upc.edu.example.example.model;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "title", length = 22, nullable = false)
    private String title;

    @Column (name = "editorial", length = 14, nullable = false)
    private String editorial;
}
