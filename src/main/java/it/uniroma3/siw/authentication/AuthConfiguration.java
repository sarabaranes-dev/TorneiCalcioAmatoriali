package it.uniroma3.siw.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import static it.uniroma3.siw.model.Credenziali.ADMIN_ROLE;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                // 2. CORRETTO: Tabelle e colonne in italiano come nel tuo database ('credenziali', 'ruolo')
                .authoritiesByUsernameQuery("SELECT username, ruolo FROM credenziali WHERE username=?")
                .usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credenziali WHERE username=?");
    }
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(auth -> auth
                        // 1. Permetti l'accesso alla rotta radice e a tutte le varianti della home
                        .requestMatchers(HttpMethod.GET, "/", "/index", "/index.html").permitAll()
                        
                        // 2. Permetti l'accesso a TUTTE le risorse statiche (CSS, JS, Immagini) e alla favicon corretta
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        
                        // 3. Permetti le rotte di registrazione e login
                        .requestMatchers("/register", "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/register", "/login").permitAll()
                        
                        //4. FUNZIONALITÀ PUBBLICHE: Permetti a tutti di vedere
                        .requestMatchers(HttpMethod.GET, "/tornei", "/tornei/**","/squadre", "/squadre/**","/giocatori", "/giocatori/**", "/partite", "/partite/**").permitAll()
                        
                        .requestMatchers(HttpMethod.GET, "/rest/**").permitAll() // Permetti a chiunque (compreso React) di leggere le partite

                        
                        // 4. Solo gli ADMIN possono accedere al pannello di controllo
                        .requestMatchers("/admin/**").hasAnyAuthority(ADMIN_ROLE)

                        // Qualsiasi altra richiesta richiede l'autenticazione
                        .anyRequest().authenticated()
                )
                // Configurazione del FORM di LOGIN
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/success", true) 
                        .failureUrl("/login?error=true")
                )
                // Configurazione del LOGOUT
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .clearAuthentication(true)
                        .permitAll()
                );
                
        return httpSecurity.build();
    }
}