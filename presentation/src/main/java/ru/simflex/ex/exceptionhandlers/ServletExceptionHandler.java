package ru.simflex.ex.exceptionhandlers;

import ru.simflex.ex.entities.User;
import ru.simflex.ex.util.constants.Actions;
import ru.simflex.ex.util.constants.Attributes;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Exception and error handler class.
 */
@WebServlet(name = "ExceptionHandler", urlPatterns = "/ExceptionHandler")
public class ServletExceptionHandler extends HttpServlet {

    /**
     * Post method from servlet container.
     *
     * @param request  Request object from servlet container
     * @param response Response object from servlet container
     * @throws ServletException from method
     * @throws IOException      from method
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processError(request, response);
    }

    /**
     * Get method from servlet container.
     *
     * @param request  Request object from servlet container
     * @param response Response object from servlet container
     * @throws ServletException from method
     * @throws IOException      from method
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processError(request, response);
    }

    /**
     * Method that processes both POST and GET method requests.
     *
     * @param request  Request object from servlet container
     * @param response Response object from servlet container
     * @throws ServletException from method
     * @throws IOException      from method
     */
    private void processError(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {


        User user = null;
        HttpSession httpSession = request.getSession(false);

        if (httpSession != null) {
            user = (User) httpSession.getAttribute(Attributes.USER);
        }

        Throwable ex = (Throwable) request
                .getAttribute("javax.servlet.error.exception");

        String uri = (String) request
                .getAttribute("javax.servlet.error.request_uri");
        if (uri == null) {
            uri = "Unknown";
        }

        if (user == null) {
            ex.printStackTrace();
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(Actions.SHOW_LOGIN_PAGE);
            requestDispatcher.forward(request, response);
        } else {
            ex.printStackTrace();
            request.setAttribute(Attributes.URI, uri);
            request.setAttribute(Attributes.EXCEPTION, ex);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(Actions.SHOW_ERROR_PAGE);
            requestDispatcher.forward(request, response);
        }
    }
}

