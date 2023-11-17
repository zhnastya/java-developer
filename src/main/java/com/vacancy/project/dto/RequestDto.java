package com.vacancy.project.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    @NotNull(message = "Значение не должно быть пустым")
    private String exp;
    @NotNull(message = "Значение не должно быть пустым")
    private String line;
    @NotNull(message = "Значение не должно быть пустым")
    private Integer column;
}
