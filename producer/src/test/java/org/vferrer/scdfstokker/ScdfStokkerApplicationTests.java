package org.vferrer.scdfstokker;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScdfStokkerApplicationTests {

	private final static String URL = "https://docs.google.com/spreadsheets/d/16CrPHjsp8MBXfK8hEYsSDQHHHp_kFCi8vuidwTv02z0/pub?output=csv";
	
	@Test
	public void contextLoads() {
	}

	
	@Test
	public void dummyTest()
	{
    	RestTemplate restTemplate = new RestTemplate();
    	
    	String csvPayload = restTemplate.getForObject(URL, String.class);
    	String lines[] = csvPayload.split("\\r?\\n");
    	
		 Arrays.asList(lines).stream()
		        // Skip header
			   .skip(1).forEach(line -> System.out.println(line));	
	}
}
