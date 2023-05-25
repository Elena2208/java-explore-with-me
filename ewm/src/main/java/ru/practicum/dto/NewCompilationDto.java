package ru.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class NewCompilationDto {
    @NotBlank
    @Size(max = 50)
    private String title;
    private Boolean pinned;
    private List<Long> events;
}
