package org.vferrer.scdfstokker.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.web.client.RestTemplate;
import org.vferrer.scdfstokker.entities.StockQuotation;
import org.vferrer.scdfstokker.entities.StockQuotationConverter;

@EnableBinding(Source.class)
@ImportResource("classpath:META-INF/liveQuotations.xml")
public class StockProducerConfig 
{
	@Value("${stocks.url}")
	private String url;
	
	@Autowired 
	StockQuotationConverter converter;
	
	Logger logger = Logger.getLogger(StockProducerConfig.class); 
	
	@InboundChannelAdapter("splitChannel")
    public List<StockQuotation> publishLatestPrices() {
    	
		logger.info("Getting stock quotes...");
    	
    	RestTemplate restTemplate = new RestTemplate();
    	
    	String csvPayload = restTemplate.getForObject(url, String.class);
    	String lines[] = csvPayload.split("\\r?\\n");
    	
    	List<StockQuotation> quotations = Arrays.asList(lines).stream().map(line -> converter.convertLiveCSVToStockQuotation(line)).collect(Collectors.toList());
    	
    	logger.info(String.format("[%d] quotes recieved", quotations.size()));
    	
        return quotations;
    }

}
