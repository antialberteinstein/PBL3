package dut.gianguhohi.shoppiefood.Models.Discounts;

import java.sql.Date;

public class Discount {
    private int discount_id;
    private String type;
    private Date time_start;
    private Date time_finish;
    private int status;
    private int discount_value;
    private boolean isPercentage;
    private int max_discount_value; 
    private int min_order_value;
    private int quantityLimit;
}
