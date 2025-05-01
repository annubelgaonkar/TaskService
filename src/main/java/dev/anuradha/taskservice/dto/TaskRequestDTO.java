package dev.anuradha.taskservice.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    private String description;

}
