package dev.animals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AnimalsApplication {

	private static Initializer initiator;

	@Autowired
	public void setInitialLoader(Initializer initiator){
		AnimalsApplication.initiator = initiator;
	}

	public static void main(String[] args) {
		SpringApplication.run(AnimalsApplication.class, args);
		initiator.initial();
	}

}
