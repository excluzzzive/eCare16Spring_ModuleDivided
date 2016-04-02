package ru.simflex.ex.filter;

import org.apache.log4j.Logger;
import ru.simflex.ex.entities.User;
import ru.simflex.ex.util.constants.Actions;
import ru.simflex.ex.util.constants.Attributes;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Authentication Filter for eCare16 project.
 */
@WebFilter(filterName = "AuthorizationFilter")
public class AuthorizationFilter implements Filter {

    /**
     * Logger instance.
     */
    private Logger logger;

    /**
     * {@inheritDoc}
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        logger = Logger.getRootLogger();
    }

    /**
     * {@inheritDoc}
     */
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);

        User user = null;
        Boolean isEmployeeView = false;
        if (session != null) {
            user = (User) session.getAttribute(Attributes.USER);

            isEmployeeView = (Boolean) session.getAttribute(Attributes.EMPLOYEE_VIEW);
            if (isEmployeeView == null) {
                isEmployeeView = false;
            }
        }

        String uri = request.getRequestURI();
//        logger.warn(uri);

        if (user == null) {

            if (uri.equals("/")) {
                response.sendRedirect(Actions.SHOW_LOGIN_PAGE);

            } else if (uri.equals(Actions.SHOW_LOGIN_PAGE) || uri.equals(Actions.EMPLOYEE_AUTHENTICATE)
                    || uri.equals(Actions.USER_AUTHENTICATE) || uri.startsWith(Actions.RESOURCES)) {

                chain.doFilter(request, response);

            } else if (uri.startsWith("/webservice/")) {
                chain.doFilter(request, response);

            } else {
                response.sendRedirect(Actions.SHOW_LOGIN_PAGE);
            }

        } else if (uri.equals("/login") || uri.equals("/")) {

            if (user.isEmployee()) {
                response.sendRedirect(Actions.SHOW_EMPLOYEE_WELCOME_PAGE);
            } else {
                response.sendRedirect(Actions.SHOW_USER_WELCOME_PAGE);
            }

        } else if (uri.equals("/logout") || uri.startsWith(Actions.RESOURCES)) {
            chain.doFilter(request, response);

        } else if (uri.startsWith("/employee") || uri.startsWith("/showEmployee")) {

            if (user.isEmployee() && isEmployeeView) {
                chain.doFilter(request, response);
            } else {
                logger.fatal("Unauthorized access to " + uri + " from: " + request.getRemoteAddr());
                response.sendRedirect(Actions.SHOW_USER_WELCOME_PAGE);
            }

        } else {
            if (isEmployeeView) {
                response.sendRedirect(Actions.SHOW_EMPLOYEE_WELCOME_PAGE);
            }
            chain.doFilter(request, response);

        }
    }

    /**
     * {@inheritDoc}
     */
    public void destroy() {
        logger = null;
    }
}
