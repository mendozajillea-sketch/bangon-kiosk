package com.bangon.kiosk.config;
import com.bangon.kiosk.entity.Customer;import com.bangon.kiosk.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;import org.springframework.context.annotation.*;import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Locale;
@Configuration public class AdminBootstrap {
 @Bean CommandLineRunner admin(CustomerRepository repo,PasswordEncoder encoder){return args->{String email=System.getenv("ADMIN_EMAIL"),password=System.getenv("ADMIN_PASSWORD");if(email==null||password==null||password.length()<12)return;if(repo.findByEmailIgnoreCase(email).isEmpty()){Customer c=new Customer();c.email=email.trim().toLowerCase(Locale.ROOT);c.name="Kiosk Administrator";c.role="ADMIN";c.passwordHash=encoder.encode(password);repo.save(c);}};}
}
