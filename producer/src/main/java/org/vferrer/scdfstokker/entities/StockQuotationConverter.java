package org.vferrer.scdfstokker.entities;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.springframework.stereotype.Component;

@Component
public class StockQuotationConverter {
	
	private static final int STOCK_COLUMN = 0;
	private static final int DATE_COLUMN = 1;
	private static final int PRICE_COLUMN = 2;
	
	public StockQuotation convertLiveCSVToStockQuotation(String input) {
 		String[] chunks = input.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		
		if (chunks.length != 3)
		{
			throw new IllegalArgumentException("Invalid CSV stock quotation: " + input);
		}
			
		StockQuotation quotation = new StockQuotation();
		quotation.setStock(chunks[STOCK_COLUMN].replaceAll("\"", ""));
		quotation.setValue(Double.parseDouble(chunks[PRICE_COLUMN].replaceAll("\"", "").replaceAll(",", ".") + "d"));
		
		// TODO Need to do this? Is the formatter not thread safe?
		DateTimeFormatterBuilder formatterBuilder = new DateTimeFormatterBuilder();
		DateTimeFormatter dateTimeFormatter = formatterBuilder.appendPattern("dd/MM/yyyy HH:mm:ss").toFormatter();
		
		DateTime time = DateTime.parse(chunks[DATE_COLUMN].replaceAll("\"", ""),dateTimeFormatter);
		Calendar calendar = new GregorianCalendar(time.getYear(), time.getMonthOfYear() -1, time.getDayOfMonth(), time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute());
		quotation.setTimestamp(calendar);
		
		return quotation;
	}

}
