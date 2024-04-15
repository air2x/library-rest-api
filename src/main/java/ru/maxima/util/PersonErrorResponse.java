package ru.maxima.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PersonErrorResponse {
    private String message;
    private LocalDateTime timestamp;
}
