package de.nsmolenskii.experiments.finleap.weather.utils.swagger;

import org.springframework.core.annotation.Order;
import springfox.bean.validators.plugins.Validators;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;

import javax.validation.constraints.NotEmpty;

import static springfox.bean.validators.plugins.Validators.annotationFromParameter;
import static springfox.bean.validators.plugins.Validators.extractAnnotation;

@Order(Validators.BEAN_VALIDATOR_PLUGIN_ORDER)
public class NotEmptyAnnotationPlugin implements ParameterBuilderPlugin, ExpandedParameterBuilderPlugin, ModelPropertyBuilderPlugin {

    @Override
    public boolean supports(final DocumentationType delimiter) {
        return true;
    }

    @Override
    public void apply(final ParameterContext context) {
        if (annotationFromParameter(context, NotEmpty.class).isPresent()) {
            context.parameterBuilder().required(true);
        }
    }

    @Override
    public void apply(final ParameterExpansionContext context) {
        if (context.findAnnotation(NotEmpty.class).isPresent()) {
            context.getParameterBuilder().required(true);
        }
    }

    @Override
    public void apply(final ModelPropertyContext context) {
        if (extractAnnotation(context, NotEmpty.class).isPresent()) {
            context.getBuilder().required(true);
        }
    }
}
