package pe.tuna.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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
}
