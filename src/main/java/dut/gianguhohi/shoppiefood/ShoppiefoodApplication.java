package dut.gianguhohi.shoppiefood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import dut.gianguhohi.shoppiefood.utils.AddressManager;
@SpringBootApplication
public class ShoppiefoodApplication {

	public static void main(String[] args) {
		AddressManager.loadAddresses();

		SpringApplication.run(ShoppiefoodApplication.class, args);
	}

}
