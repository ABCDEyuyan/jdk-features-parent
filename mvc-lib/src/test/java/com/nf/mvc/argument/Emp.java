package com.nf.mvc.argument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Emp {
    private int id;
    private String empName;
    private BigDecimal salary;
    private boolean gender;
}
