package example.cashcard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// SecurityConfig is a Java Bean TODO what is Java Bean?.
@Configuration // Instructs Spring to use this class to configure Spring and Spring Boot. Any Beans specified in this class will now be available to Spring's Auto Configuration engine.
class SecurityConfig {

    // Spring Security expects a Bean to configure its Filter Chain.
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request.requestMatchers("/cashcards/**").hasRole("CARD-OWNER")) // RBAC authorization
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable()); // CSRF disabled cuz API service only used by non-browser clients
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
        // Builder pattern.
        User.UserBuilder users = User.builder();
        UserDetails sarah = users
            .username("sarah1")
            .password(passwordEncoder.encode("abc123"))
            .roles("CARD-OWNER")
            .build();
        UserDetails hankOwnsNoCards = users
            .username("hank-owns-no-cards")
            .password(passwordEncoder.encode("qrs456"))
            .roles("NON-OWNER")
            .build();
        return new InMemoryUserDetailsManager(sarah, hankOwnsNoCards);
    }
}