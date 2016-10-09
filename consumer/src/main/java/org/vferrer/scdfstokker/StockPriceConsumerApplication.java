package org.vferrer.scdfstokker;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.vferrer.scdfstokker.entities.StockQuotation;

@SpringBootApplication
@EnableBinding(Sink.class)
public class StockPriceConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockPriceConsumerApplication.class, args);
	}
	
	
    @StreamListener(Sink.INPUT)
    public void log(List<StockQuotation> messages) 
    {
    	messages.stream().forEach(quotation -> System.out.println(quotation.toString()));
    }

}

