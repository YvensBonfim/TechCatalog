package com.yvens.techcatalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.yvens.techcatalog.Service.UserService;

@SpringBootApplication
public class TechcatalogApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TechcatalogApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		
		
	}

}
