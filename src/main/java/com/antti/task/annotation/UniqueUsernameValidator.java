package com.antti.task.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.antti.task.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> 
{
    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        // Ignore
    }

    @Override
    public boolean isValid(String t, ConstraintValidatorContext cvc) {
        if (this.userRepository == null) {
            return false;
        }
        return this.userRepository.findByName(t) == null;
    }
}