package backend.likelion.todos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class TodosApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodosApplication.class, args);
    }

}
