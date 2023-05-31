package ru.practicum.dto.compilation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Builder
public class NewCompilationDto {
    private List<Long> events;
    private boolean pinned;
    @NotBlank
    @Length(max = 50)
    private String title;
}
