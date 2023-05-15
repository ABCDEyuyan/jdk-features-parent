package com.nf.entity;

import com.nf.mvc.file.MultipartFile;

import java.math.BigDecimal;

/**
 * @ClassName Product
 * @Author ZL
 * @Date 2023/5/9 20:08
 * @Version 1.0
 * @Explain
 **/
public class ProductEntity {
    private Integer id;
    private String name;
    private String image;
    private BigDecimal price;
    private Integer count;
    private Integer cid;
    private Boolean status;
    private MultipartFile multipartFile;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", cid=" + cid +
                ", status=" + status +
                ", multipartFile=" + multipartFile +
                '}';
    }
}
