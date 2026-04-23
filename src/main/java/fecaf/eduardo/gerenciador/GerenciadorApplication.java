package fecaf.eduardo.gerenciador;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GerenciadorApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(GerenciadorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {}
}
