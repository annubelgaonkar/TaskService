package dev.anuradha.taskservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String createdBy;
}
