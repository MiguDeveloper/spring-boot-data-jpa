package pe.tuna.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.tuna.app.auth.handler.LoginSuccesHandler;
import pe.tuna.app.models.services.JpaUserDetailsService;

import javax.sql.DataSource;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginSuccesHandler successHandler;

    // Como tenemos registrado en el contenedor pasamos a inyectar para obtener el metodo
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Para tener acceso a la base de datos generamos el datasource
    @Autowired
    private DataSource dataSource;

    @Autowired
    private JpaUserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar**", "/locale", "/api/clientes/**").permitAll()
                .antMatchers("/ver/**").hasAnyRole("USER")
                .antMatchers("/upload/**").hasAnyRole("USER")
                .antMatchers("/form/**").hasAnyRole("ADMIN")
                .antMatchers("/eliminar/**").hasAnyRole("ADMIN")
                .antMatchers("/factura/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                        .successHandler(successHandler)
                        .loginPage("/login")
                    .permitAll()
                .and()
                    .logout().permitAll()
                .and()
                    .exceptionHandling().accessDeniedPage("/error_403");
    }

    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {

        // Implementacion de autenticacion con JPA
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        // Implementacion de autenticacion con JDBC
        /*
        builder.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery("SELECT username, password, enable FROM users WHERE username = ?")
                .authoritiesByUsernameQuery("SELECT u.username, a.authority FROM authorities a INNER JOIN users u ON a.user_id = u.id WHERE u.username = ?");
         */

        /*
        PasswordEncoder encoder = this.passwordEncoder;
        User.UserBuilder users = User.builder().passwordEncoder(encoder::encode);// Forma abreviada expresion lambda

        builder.inMemoryAuthentication()
                .withUser(users.username("admin").password("12345").roles("ADMIN", "USER"))
                .withUser(users.username("miguel").password("54321").roles("USER"));

         */
    }
}
