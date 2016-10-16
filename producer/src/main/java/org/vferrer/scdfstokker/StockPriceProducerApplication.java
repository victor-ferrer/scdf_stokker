package org.vferrer.scdfstokker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.vferrer.scdfstokker.config.StockProducerConfig;

@SpringBootApplication
@Import(StockProducerConfig.class)
public class StockPriceProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockPriceProducerApplication.class, args);
	}

}

