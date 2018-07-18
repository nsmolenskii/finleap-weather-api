package de.nsmolenskii.experiments.finleap.weather.config;

import com.fasterxml.classmate.TypeResolver;
import de.nsmolenskii.experiments.finleap.weather.utils.swagger.NotEmptyAnnotationPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.JacksonSerializerConvention;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static de.nsmolenskii.experiments.finleap.weather.FinleapWeatherApiApplication.ROOT_PACKAGE;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

@Configuration
@EnableSwagger2
@Import({
        NotEmptyAnnotationPlugin.class,
        BeanValidatorPluginsConfiguration.class,
})
public class SwaggerConfig {

    private final TypeResolver typeResolver;

    public SwaggerConfig(final TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    @Bean
    public Docket petApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // api selector
                .select()
                .apis(basePackage(ROOT_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                //model substitution
                .alternateTypeRules(newRule(
                        typeResolver.resolve(Mono.class, typeResolver.resolve(WildcardType.class)),
                        typeResolver.resolve(WildcardType.class)
                ))
                .alternateTypeRules(newRule(
                        typeResolver.resolve(Flux.class, typeResolver.resolve(WildcardType.class)),
                        typeResolver.resolve(List.class, WildcardType.class)
                ));
    }

    @Bean
    public AlternateTypeRuleConvention jacksonSerializerConvention(final TypeResolver resolver) {
        return new JacksonSerializerConvention(resolver, ROOT_PACKAGE);
    }

}
