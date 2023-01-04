package multi.tasklet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringMultiTaskletApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringMultiTaskletApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
