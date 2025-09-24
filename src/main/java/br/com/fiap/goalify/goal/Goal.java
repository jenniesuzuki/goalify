package br.com.fiap.goalify.goal;

import br.com.fiap.goalify.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{goal.title.notblank}")
    private String title;

    @Size(min = 20, max = 255, message = "{goal.description.size}")
    private String description;

    @Min(value = 1, message = "{goal.effort.min}")
    @Max(value = 100, message = "{goal.effort.max}")
    private int effort;

    @Min(0) @Max(100)
    private int status;

    @ManyToOne
    private User user;
}
