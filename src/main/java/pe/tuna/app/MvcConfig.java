package pe.tuna.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;


@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /*
    private static Logger logger = LoggerFactory.getLogger(MvcConfig.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
        logger.info("El resourcePath del MvcConfig es: " + resourcePath);
        registry.addResourceHandler("/uploads/**").addResourceLocations(resourcePath);
        // Anterior mente manejamos toda la ruta de la carpeta local file:///Users/miguelchinchay/Documents/img_spring-boot/
        // ahora manejamos una carpeta dentro del proyecto
    }
     */

    // Es importante que cuando este metodo este en una clase de configuracion
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Registramos un controlador de vistas: viewController, Ojo el metodo tiene que llamarse igual
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/error_403").setViewName("error_403");
    }

}
