package ru.kpfu.itis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@PropertySource("classpath:file.properties")
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:static/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/home");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/not_confirmed").setViewName("not_confirmed_user");
        registry.addViewController("/banned").setViewName("banned");
        registry.addViewController("/deleted").setViewName("deleted");
        registry.addViewController("/bad_csrf").setStatusCode(HttpStatus.CONFLICT).setViewName("bad_csrf");
    }
}
