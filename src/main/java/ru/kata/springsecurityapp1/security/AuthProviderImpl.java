package ru.kata.springsecurityapp1.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.kata.springsecurityapp1.services.PersonDetailsService;

import java.util.Collections;

@Component
public class AuthProviderImpl implements AuthenticationProvider {


    private final PersonDetailsService personDetailsService;

    @Autowired
    public AuthProviderImpl(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }


    // Данный метод вызывает сам security
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();

        UserDetails personDetails = personDetailsService.loadUserByUsername(username);


        // получаем паль с authentication
        String password = authentication.getCredentials().toString();

        // теперь нужно сравни

        if (password.equals(personDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }


        // если пароли совпали, возвращаем personDetails самого человека  (personDetails -> PRINCIPAL)
        // еще список прав Collections.emptyList()/ - в будущем вернем

        // UsernamePasswordAuthenticationToken - реализует Authentication - по этому, можем его вернуть
        return new UsernamePasswordAuthenticationToken(personDetails, password,
                Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
