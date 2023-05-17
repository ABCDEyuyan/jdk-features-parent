package com.nf.demo.vo;

import com.nf.demo.entity.PaginationText;
import com.nf.demo.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedProductVO {
    private PaginationText paginationText;
    private List<ProductEntity> list;
}
