package coms309;

import coms309.Users.UserRepository;
import coms309.Websockets.WebSocketConfig;
import coms309.Websockets.WebSocketServer;
import coms309.Workout.WorkoutRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;


@SpringBootApplication
public class Application {
	
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
    @Bean
    CommandLineRunner initUser(UserRepository userRepository, WorkoutRepository workoutRepository) {
        return args -> {

        };
    }

}
