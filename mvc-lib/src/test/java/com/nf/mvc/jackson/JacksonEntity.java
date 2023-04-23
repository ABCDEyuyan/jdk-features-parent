package com.nf.mvc.jackson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JacksonEntity {
    private int id;
    private String name;
    private LocalDate birthday;
}