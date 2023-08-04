package com.mindhub.homebanking;

import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.mindhub.homebanking.models.Client;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}

	@Bean
	public CommandLineRunner init(ClientRepository clientRepository){
		return args -> {

			Client client = new Client("Melba", "Morel", "melba@mindhub.com");
			System.out.println(client);
			clientRepository.save(client);
			System.out.println(client);
			clientRepository.save(new Client("Oreo", "Milka", "oreo@Choko.com"));
		};
	}

}
