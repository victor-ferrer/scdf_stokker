package org.vferrer.scdfstokker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.app.httpclient.processor.HttpclientProcessorConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@Import(HttpclientProcessorConfiguration.class)
@ImportResource("classpath:META-INF/httpTrigger.xml")
public class HistoricalProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HistoricalProducerApplication.class, args);
	}
}
