package nx.ese.config;

import nx.ese.dtos.validators.NxPattern;
import nx.ese.utils.LocalDateConverter;
import nx.ese.utils.LocalDateTimeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableMongoAuditing
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
/*        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("http://192.168.1.40:4200", "http://192.168.1.90:4200")
                .maxAge(3600);*/
        registry.addMapping("/**").allowedMethods("*").allowedOrigins("*").maxAge(3600);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new LocalDateConverter(NxPattern.LOCAL_DATE_FORMAT));
        registry.addConverter(new LocalDateTimeConverter(NxPattern.LOCAL_DATE_TIME_FORMAT));
    }

}
