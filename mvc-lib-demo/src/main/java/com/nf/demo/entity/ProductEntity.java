package com.nf.demo.entity;

import com.nf.mvc.file.MultipartFile;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductEntity {
    private Integer id;
    private String pname;
    private String image;
    private Integer qty;
    private Boolean status;
    private LocalDate pubdate;
    private BigDecimal price;
    private Integer cid;
    private MultipartFile pfile;

    public String getStatusText(){
        return this.status==true?"上架":"下架";
    }

}
