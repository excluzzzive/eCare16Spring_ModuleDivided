package ru.simflex.ex.exceptionhandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.simflex.ex.entities.User;
import ru.simflex.ex.constants.Attributes;
import ru.simflex.ex.constants.Pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Exception handler by the Spring.
 */
//@ControllerAdvice()
public class GlobalExceptionHandler {

    /**
     * HttpSession instance.
     */
    @Autowired
    private HttpSession httpSession;

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Throwable.class)
    public String handleException(HttpServletRequest request, Exception ex, Model model) {

        User user = null;

        if (httpSession != null) {
            user = (User) httpSession.getAttribute(Attributes.USER);
        }

        if (user == null) {
            ex.printStackTrace();
            model.addAttribute(Attributes.UNKNOWN_ERROR, true);
            return Pages.LOGIN_PAGE;
        } else {
            ex.printStackTrace();
            model.addAttribute(Attributes.URI, request.getRequestURI());
            model.addAttribute(Attributes.EXCEPTION, ex);
            return Pages.ERROR_PAGE;
        }
    }

}
