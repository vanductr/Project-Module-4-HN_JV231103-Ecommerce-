package rikkei.academy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import rikkei.academy.model.entity.Role;
import rikkei.academy.model.entity.RoleName;
import rikkei.academy.model.entity.User;
import rikkei.academy.repository.IUserRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner runner(PasswordEncoder passwordEncoder, IUserRepository userRepository) {
//        return args -> {
//            Role admin = new Role(null, RoleName.ROLE_ADMIN);
//            Role manager = new Role(null, RoleName.ROLE_MANAGER);
//            Role user = new Role(null, RoleName.ROLE_USER);
//
//            Set<Role> roleSet = new HashSet<>();
//            roleSet.add(admin);
//            roleSet.add(manager);
//            roleSet.add(user);
//
//            User user1 = new User(null, "admin", "admin@gmail.com", "Trần Văn ADMIN", true, passwordEncoder.encode("admin"), null, "0972362176", "Hà Nội Việt Nam", LocalDate.now(), null, false, roleSet);
//
//            userRepository.save(user1);
//        };
//    }
}
