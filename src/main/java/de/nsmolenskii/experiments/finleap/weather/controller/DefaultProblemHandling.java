package de.nsmolenskii.experiments.finleap.weather.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.validation.Violation;

import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestControllerAdvice
public class DefaultProblemHandling implements ProblemHandling {

    @Override
    public URI defaultConstraintViolationType() {
        return URI.create("/problem/constraint-violation");
    }

    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException exception,
            final NativeWebRequest request
    ) {
        return newConstraintViolationProblem(exception, createViolations(exception.getBindingResult()), request);
    }

    @Override
    public ResponseEntity<Problem> handleConstraintViolation(final ConstraintViolationException exception, final NativeWebRequest request) {
        final List<Violation> violations = exception.getConstraintViolations().stream()
                .map(this::createViolation)
                .collect(toList());

        return newConstraintViolationProblem(exception, violations, request);
    }
}
