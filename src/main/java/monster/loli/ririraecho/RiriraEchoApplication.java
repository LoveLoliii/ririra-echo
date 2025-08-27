package monster.loli.ririraecho;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("monster.loli.ririraecho.mapper")
public class RiriraEchoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RiriraEchoApplication.class, args);
    }

}
