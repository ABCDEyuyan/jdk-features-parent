package com.nf.utils.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName ResponseVo
 * @Author ZL
 * @Date 2023/5/9 8:59
 * @Version 1.0
 * @Explain
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseVo {
    private Integer id;
    private String msg;
    private Object data;
}
