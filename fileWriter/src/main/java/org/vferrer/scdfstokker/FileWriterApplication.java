package org.vferrer.scdfstokker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.vferrer.scdfstokker.config.FileSinkConfiguration;

@SpringBootApplication
@Import(FileSinkConfiguration.class)
@ImportResource("classpath:META-INF/entryConverter.xml")
public class FileWriterApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileWriterApplication.class, args);
	}
}
