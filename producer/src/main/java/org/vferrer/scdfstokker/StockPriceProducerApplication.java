package org.vferrer.scdfstokker;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.web.client.RestTemplate;
import org.vferrer.scdfstokker.entities.StockQuotation;

@SpringBootApplication
@EnableBinding(Source.class)
public class StockPriceProducerApplication {

	private final static String URL = "https://docs.google.com/spreadsheets/d/16CrPHjsp8MBXfK8hEYsSDQHHHp_kFCi8vuidwTv02z0/pub?output=csv";
		
	private static final int STOCK_COLUMN = 0;
	private static final int DATE_COLUMN = 1;
	private static final int PRICE_COLUMN = 2;
	
	public static void main(String[] args) {
		SpringApplication.run(StockPriceProducerApplication.class, args);
	}
	
	@Autowired
	DateTimeFormatter datetimeFormatter;
	
	@Bean
	public DateTimeFormatter dateTimeFormatter () {
		DateTimeFormatterBuilder formatterBuilder = new DateTimeFormatterBuilder();
		return formatterBuilder.appendPattern("dd/MM/yyyy HH:mm:ss").toFormatter();
	}
	
    @InboundChannelAdapter(Source.OUTPUT)
    public List<StockQuotation> publishLatestPrices() {
    	
    	System.out.print("Getting stock quotes...");
    	
    	RestTemplate restTemplate = new RestTemplate();
    	
    	String csvPayload = restTemplate.getForObject(URL, String.class);
    	String lines[] = csvPayload.split("\\r?\\n");
    	
    	List<StockQuotation> quotations = Arrays.asList(lines).stream().map(line -> convertLiveCSVToStockQuotation(line)).collect(Collectors.toList());
    	
    	System.out.println(String.format("[%d] quotes recieved", quotations.size()));
    	
        return quotations;
    }
    
	private StockQuotation convertLiveCSVToStockQuotation(String input) {
 		String[] chunks = input.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		
		if (chunks.length != 3)
		{
			throw new IllegalArgumentException("Invalid CSV stock quotation: " + input);
		}
			
		StockQuotation quotation = new StockQuotation();
		quotation.setStock(chunks[STOCK_COLUMN].replaceAll("\"", ""));
		quotation.setValue(Double.parseDouble(chunks[PRICE_COLUMN].replaceAll("\"", "").replaceAll(",", ".") + "d"));
		
		DateTime time = DateTime.parse(chunks[DATE_COLUMN].replaceAll("\"", ""),datetimeFormatter);
		Calendar calendar = new GregorianCalendar(time.getYear(), time.getMonthOfYear() -1, time.getDayOfMonth(), time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute());
		quotation.setTimestamp(calendar);
		
		return quotation;
	}

}

