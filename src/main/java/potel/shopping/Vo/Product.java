package potel.shopping.Vo;

import lombok.Data;

@Data
public class Product {
    private int prdId;          // 商品ID
    private String prdName;     // 商品名稱
    private int price;          // 價格
    private int imageId;        // 圖片ID
    private String prdDesc;     // 商品描述
    private String prdtype;     // 商品分類
	private char status;        // 狀態
    private java.sql.Timestamp createDate;  // 創建日期
    private java.sql.Timestamp modifyDate;  // 修改日期

//    public Product() {
//        // 預設建構子
//    }

//    public Product(int prdId, String prdName, int price, int imageId, String prdDesc, String prdtype, char status, java.sql.Timestamp createDate, java.sql.Timestamp modifyDate) {
//        this.prdId = prdId;
//        this.prdName = prdName;
//        this.price = price;
//        this.imageId = imageId;
//        this.prdDesc = prdDesc;
//        this.prdtype = prdtype;
//        this.status = status;
//        this.createDate = createDate;
//        this.modifyDate = modifyDate;
//    }

//    @Override
//    public String toString() {
//        return "Product ID: " + prdId + ", Name: " + prdName + ", Price: " + price;
//    }
//
//    // Getters 和 Setters
//    public int getPrdId() {
//        return prdId;
//    }
//
//    public void setPrdId(int prdId) {
//        this.prdId = prdId;
//    }
//
//    public String getPrdName() {
//        return prdName;
//    }
//
//    public void setPrdName(String prdName) {
//        this.prdName = prdName;
//    }
//
//    public int getPrice() {
//        return price;
//    }
//
//    public void setPrice(int price) {
//        this.price = price;
//    }
//
//    public int getImageId() {
//        return imageId;
//    }
//
//    public void setImageId(int imageId) {
//        this.imageId = imageId;
//    }
//
//    public String getPrdDesc() {
//        return prdDesc;
//    }
//
//    public void setPrdDesc(String i) {
//        this.prdDesc = i;
//    }
//
//    public char getStatus() {
//        return status;
//    }
//
//    public void setStatus(char status) {
//        this.status = status;
//    }
//
//    public java.sql.Timestamp getCreateDate() {
//        return createDate;
//    }
//
//    public void setCreateDate(java.sql.Timestamp createDate) {
//        this.createDate = createDate;
//    }
//
//    public java.sql.Timestamp getModifyDate() {
//        return modifyDate;
//    }
//
//    public void setModifyDate(java.sql.Timestamp modifyDate) {
//        this.modifyDate = modifyDate;
//    }
}