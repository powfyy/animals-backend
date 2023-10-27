package dev.pethaven;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PetHavenApplication {
	private static Initializer initiator;
	@Autowired
	public void setInitialLoader(Initializer initiator){
		PetHavenApplication.initiator= initiator;
	}

	public static void main(String[] args) {
		SpringApplication.run(PetHavenApplication.class, args);
		initiator.initial();
	}

}
