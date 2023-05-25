package ru.practicum;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ViewStatsDto {
    private String app;
    private String uri;
    private long hits;
}
