package ru.simflex.ex.aspects;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import ru.simflex.ex.entities.*;

/**
 * Logging aspect for UserService.
 */

@Aspect
public class GenericLoggingAspect {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getRootLogger();

    @Before("@annotation(ru.simflex.ex.annotations.Loggable)")
    public void logLoggable(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        String argsString = "";

        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Contract) {

                Contract contract = (Contract) args[i];
                argsString += "Contract(id:" + contract.getId() + ";phoneNumber:"
                        + contract.getPhoneNumber().getPhoneNumberString() + ")";

            } else if (args[i] instanceof Option) {

                Option option = (Option) args[i];
                argsString += "Option(id:" + option.getId() + ";name:" + option.getName() + ")";

            } else if (args[i] instanceof PhoneNumber) {

                PhoneNumber phoneNumber = (PhoneNumber) args[i];
                argsString += "PhoneNumber(id:" + phoneNumber.getId() + ";phoneNumberString:"
                        + phoneNumber.getPhoneNumberString() + ")";

            } else if (args[i] instanceof Tariff) {

                Tariff tariff = (Tariff) args[i];
                argsString += "Tariff(id:" + tariff.getId() + ";name:" + tariff.getName() + ")";

            } else if (args[i] instanceof User) {

                User user = (User) args[i];
                argsString += "User(id:" + user.getId() + ";email:" + user.getEmail() + ")";

            } else {
                argsString += args[i];
            }
            if (i != args.length - 1) {
                argsString += ", ";
            }
        }
        logger.warn(String.format("Method %s was called with args: %s", methodName, argsString));
    }

    @Before("execution(* ru.simflex.ex.services.implementation.UserServiceImplementation.authUser(..))")
    public void logAuthentication(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        logger.warn(String.format("User %s trying to authenticate", args[0]));

    }

    @AfterThrowing(value = "execution(* ru.simflex.ex.services.implementation.*.*(..))", throwing = "ex")
    public void logExceptions(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        logger.error("Exception thrown in method " + methodName + ", message: " + ex.getMessage());
    }

}
