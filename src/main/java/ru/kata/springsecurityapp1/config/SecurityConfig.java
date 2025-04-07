package ru.kata.springsecurityapp1.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.springsecurityapp1.services.PersonDetailsService;
//import ru.kata.springsecurityapp1.security.AuthProviderImpl;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // конфигурируем сам Spring Security (какая страничка отвечает за вход, какая за ошибки)
        // конфигурируем авторизацию

        //настройка страницы логина
        http.csrf().disable()                                                       // временно отключаем токен защиту от межсайтовой подделки запросов
                .authorizeRequests()
                .antMatchers("/auth/login","/auth/registration", "/error").permitAll()               // пускаем любого пользователя на эти две станицы
                .anyRequest().authenticated()                                       // на все остальные, только авторизованных
                .and()                                                              // конкатенация настроек
                .formLogin()
                .loginPage("/auth/login")
                .loginProcessingUrl("/process_login")                              // автоматически присылает для сравнения пароля
                .defaultSuccessUrl("/hello", true)           // после успешной аутентификации перенаправляется на "/hello"
                .failureUrl("/auth/login?error");                // не успешная аутентиф.
    }

    // настройка аутентификации
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailsService);
    }

    // алгоритм расшифровки пароля
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // не шифруем
    }

    //----------------------------------------------------------

//    private final AuthProviderImpl authProvider;
//
//    @Autowired
//    public SecurityConfig(AuthProviderImpl authProvider) {
//        this.authProvider = authProvider;
//    }

    // Настраивает аутентификацию
//    protected void configure(AuthenticationManagerBuilder auth) {
//        auth.authenticationProvider(authProvider);
//    }
}