package dut.gianguhohi.shoppiefood.Models.Discounts;

import java.sql.Date;

public class Discount {
    private int discount_id;
    private String typeDiscount;
    private Date time_start;
    private Date time_finish;
    private int status;
    private int value;
    private int percent;
    private int quantityLimit; //Gioi han su nguoi duoc su dung ma khuyen mai nhanh nhat
    private int minOrderValue; //Gia tri toi thieu de ap dung ma khuyen mai
    private int maxDiscount; //Gioi han giam gia toi da cho 1 san pham
}
