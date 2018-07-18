package de.nsmolenskii.experiments.finleap.weather.config;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.zalando.problem.ProblemModule;

@Configuration
@Import({ProblemModule.class, JavaTimeModule.class, Jdk8Module.class})
public class WebMvcConfig implements WebMvcConfigurer {
}
