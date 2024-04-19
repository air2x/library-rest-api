package ru.maxima.util.exeptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BookErrorResponse {
    private String message;
    private LocalDateTime timestamp;
}
