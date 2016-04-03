package ru.simflex.ex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.simflex.ex.entities.Option;
import ru.simflex.ex.entities.Tariff;
import ru.simflex.ex.entities.User;
import ru.simflex.ex.services.interfaces.OptionService;
import ru.simflex.ex.services.interfaces.TariffService;
import ru.simflex.ex.services.interfaces.UserService;
import ru.simflex.ex.constants.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Login controller class.
 */
@Controller
public class LoginController {

    /**
     * UserService instance.
     */
    @Autowired
    private UserService userService;

    /**
     * OptionService instance.
     */
    @Autowired
    private OptionService optionService;

    /**
     * TariffService instance.
     */
    @Autowired
    private TariffService tariffService;

    /**
     * HttpSession instance.
     */
    @Autowired
    private HttpSession httpSession;

    /**
     * Login action.
     *
     * @param params Request parameters
     * @param model  Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.SHOW_LOGIN_PAGE)
    public String showLoginPage(@RequestParam Map<String, String> params, Model model) {

        if (params.get(Parameters.EMPLOYEE_VIEW) != null) {
            model.addAttribute(Attributes.EMPLOYEE_VIEW, true);
        }

        return Pages.LOGIN_PAGE;
    }

    /**
     * Show user welcome page action.
     *
     * @param model Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.SHOW_USER_WELCOME_PAGE)
    public String showUserWelcomePage(Model model) {

        List<Tariff> tariffList = tariffService.getTariffList();
        model.addAttribute(Attributes.TARIFF_LIST, tariffList);

        List<Option> optionList = optionService.getOptionList();
        model.addAttribute(Attributes.OPTION_LIST, optionList);

        return Pages.USER_WELCOME_PAGE;
    }

    /**
     * User authenticate action.
     *
     * @param params Request parameters
     * @param model  Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.USER_AUTHENTICATE)
    public String userAuthenticate(@RequestParam Map<String, String> params, Model model) throws IOException {

        String email = params.get(Parameters.EMAIL);
        String password = params.get(Parameters.PASSWORD);

        User user = (User) httpSession.getAttribute(Attributes.USER);

        if (user == null) {

            if (email == null || password == null) {
                return Pages.LOGIN_PAGE;
            }

            User authUser = userService.authUser(email, password);

            if (authUser != null) {
                httpSession.setAttribute(Attributes.USER, authUser);

                List<Tariff> tariffList = tariffService.getTariffList();
                model.addAttribute(Attributes.TARIFF_LIST, tariffList);

                List<Option> optionList = optionService.getOptionList();
                model.addAttribute(Attributes.OPTION_LIST, optionList);

                return Pages.USER_WELCOME_PAGE;

            } else {
                model.addAttribute(Attributes.USER_INVALID_AUTH, true);
                return Pages.LOGIN_PAGE;
            }
        } else {
            return Pages.EMPLOYEE_WELCOME_PAGE;
        }
    }

    /**
     * Show employee welcome page action.
     *
     * @return View to redirect
     */
    @RequestMapping(value = Actions.SHOW_EMPLOYEE_WELCOME_PAGE)
    public String showEmployeeWelcomePage() {
        return Pages.EMPLOYEE_WELCOME_PAGE;
    }

    @RequestMapping(value = Actions.EMPLOYEE_AUTHENTICATE)
    public String employeeAuthenticate(@RequestParam Map<String, String> params, Model model) throws IOException {

        String email = params.get(Parameters.EMAIL);
        String password = params.get(Parameters.PASSWORD);

        User user = (User) httpSession.getAttribute(Attributes.USER);

        if (user == null) {

            if (email == null || password == null) {
                return Pages.LOGIN_PAGE;
            }

            User authUser = userService.authUser(email, password);

            if (authUser != null) {

                if (authUser.isEmployee()) {
                    httpSession.setAttribute(Attributes.USER, authUser);
                    httpSession.setAttribute(Attributes.EMPLOYEE_VIEW, true);
                    return Pages.EMPLOYEE_WELCOME_PAGE;

                } else {
                    model.addAttribute(Attributes.EMPLOYEE_VIEW, true);
                    model.addAttribute(Attributes.USER_IS_NOT_EMPLOYEE, true);
                    return Pages.LOGIN_PAGE;
                }

            } else {
                model.addAttribute(Attributes.EMPLOYEE_VIEW, true);
                model.addAttribute(Attributes.EMPLOYEE_INVALID_AUTH, true);
                return Pages.LOGIN_PAGE;
            }
        } else {
            return Pages.EMPLOYEE_WELCOME_PAGE;
        }
    }

    /**
     * Logout action.
     *
     * @param model Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.LOGOUT)
    public String logout(Model model) {

        User user = (User) httpSession.getAttribute(Attributes.USER);

        if (user.isEmployee()) {

            Boolean isEmployeeView = (Boolean) httpSession.getAttribute(Attributes.EMPLOYEE_VIEW);
            if (isEmployeeView == null) {
                isEmployeeView = false;
            }

            if (isEmployeeView) {
                model.addAttribute(Attributes.EMPLOYEE_VIEW, true);
            }
        }

        httpSession.removeAttribute(Attributes.EMPLOYEE_VIEW);
        httpSession.removeAttribute(Attributes.USER);
        return Pages.LOGIN_PAGE;
    }

    /**
     * Show error page action.
     *
     * @param model   Model object
     * @param request Request object
     * @return View to redirect
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @RequestMapping(value = Actions.SHOW_ERROR_PAGE)
    public String showErrorPage(Model model, HttpServletRequest request) {

        Throwable ex = (Throwable) request.getAttribute(Attributes.EXCEPTION);
        String uri = (String) request.getAttribute(Attributes.URI);

        model.addAttribute(Attributes.EXCEPTION, ex);
        model.addAttribute(Attributes.URI, uri);

        return Pages.ERROR_PAGE;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(value = Actions.SHOW_404_PAGE)
    public String show404Page(Model model) {
        model.addAttribute(Attributes.ERROR_CODE, 404);
        return Pages.ERROR_PAGE;
    }
}
