package ru.simflex.ex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.simflex.ex.entities.Contract;
import ru.simflex.ex.entities.User;
import ru.simflex.ex.exceptions.UserReadingException;
import ru.simflex.ex.services.interfaces.UserService;
import ru.simflex.ex.util.constants.Actions;
import ru.simflex.ex.util.constants.Attributes;
import ru.simflex.ex.util.constants.Pages;
import ru.simflex.ex.util.constants.Parameters;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User controller class.
 */

@Controller
public class UserController {

    /**
     * UserService instance.
     */
    @Autowired
    private UserService userService;

    /**
     * Birth date format.
     */
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * HttpSession instance.
     */
    @Autowired
    private HttpSession httpSession;


    /**
     * My custom binder.
     * Added new date format for birthDate field
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {

        binder.registerCustomEditor(Date.class, "birthDate", new CustomDateEditor(dateFormat, false));
    }

    /**
     * Method showing page with list of users.
     *
     * @param params Request parameters
     * @param model  Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.SHOW_EMPLOYEE_USER_LIST_PAGE)
    public String showUserListPage(@RequestParam Map<String, String> params, Model model) {

        List<User> userList = userService.getUserList();
        model.addAttribute(Attributes.USER_LIST, userList);

        return Pages.EMPLOYEE_USER_LIST_PAGE;
    }

    /**
     * Method showing page with create user form.
     *
     * @return View to redirect
     */
    @RequestMapping(value = Actions.SHOW_EMPLOYEE_USER_CREATE_PAGE)
    public String showCreateUserPage() {
        return Pages.EMPLOYEE_USER_CREATE_PAGE;
    }

    /**
     * Methods showing page with edit user form
     *
     * @param phoneNumberString Phone number that belongs to user
     * @param model             Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.SHOW_EMPLOYEE_EDIT_USER_BY_PHONE_NUMBER_PAGE, method = RequestMethod.POST)
    public String editUserByPhoneNumber(@RequestParam(Parameters.EDITED_USER_PHONE_NUMBER) String phoneNumberString,
                                        Model model) {

        List<User> userList = userService.getUserList();

        User editedUser = null;
        List<Contract> userContractList = null;
        String editedUserBirthDate = null;

        try {
            editedUser = userService.getUserByPhoneNumber(phoneNumberString);
            editedUser.setPassword("");

            userContractList = userService.getUserContractListById(Integer.toString(editedUser.getId()));
            editedUserBirthDate = dateFormat.format(editedUser.getBirthDate());

        } catch (UserReadingException e) {
            model.addAttribute(Attributes.USER_NOT_FOUND_BY_PHONE_NUMBER, true);
        }

        model.addAttribute(Attributes.USER_LIST, userList);
        model.addAttribute(Attributes.EDITED_USER, editedUser);
        model.addAttribute(Attributes.USER_CONTRACT_LIST, userContractList);
        model.addAttribute(Attributes.EDITED_USER_BIRTH_DATE, editedUserBirthDate);

        return Pages.EMPLOYEE_USER_LIST_PAGE;
    }

    /**
     * Methods showing page with edit user form
     *
     * @param idString Id of user
     * @param model    Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.SHOW_EMPLOYEE_EDIT_USER_BY_ID_PAGE, method = RequestMethod.POST)
    public String editUserById(@RequestParam(Parameters.EDITED_USER_ID) String idString, Model model) {

        List<User> userList = userService.getUserList();

        User editedUser = userService.getUserById(idString);

        if (editedUser != null) {
            editedUser.setPassword("");
            List<Contract> userContractList = userService.getUserContractListById(idString);
            model.addAttribute(Attributes.USER_CONTRACT_LIST, userContractList);
            model.addAttribute(Attributes.EDITED_USER_BIRTH_DATE, dateFormat.format(editedUser.getBirthDate()));
            model.addAttribute(Attributes.EDITED_USER, editedUser);
        }

        model.addAttribute(Attributes.USER_LIST, userList);
        return Pages.EMPLOYEE_USER_LIST_PAGE;
    }

    /**
     * Create user action.
     *
     * @param newUser User object
     * @param result  Result of binding
     * @return View to redirect
     */
    @RequestMapping(value = Actions.EMPLOYEE_CREATE_USER, method = RequestMethod.POST)
    public ModelAndView createUser(@Valid @ModelAttribute(Attributes.NEW_USER) User newUser, BindingResult result) {

        ModelAndView modelAndView = new ModelAndView(Pages.EMPLOYEE_USER_CREATE_PAGE);
        modelAndView.addObject(Attributes.NEW_USER_BIRTH_DATE, dateFormat.format(newUser.getBirthDate()));

        if (result.hasErrors()) {
            return modelAndView;
        }

        //Checks whether email and passport data are unique
        User sameEmailUser = userService.getUserByEmail(newUser.getEmail());
        User samePassportDataUser = userService.getUserByPassportData(newUser.getPassportData());

        if (samePassportDataUser != null) {
            modelAndView.addObject(Attributes.NOT_UNIQUE_PASSPORT_DATA, Attributes.NOT_UNIQUE_PASSPORT_DATA);
            return modelAndView;
        }
        if (sameEmailUser != null) {
            modelAndView.addObject(Attributes.NOT_UNIQUE_EMAIL, Attributes.NOT_UNIQUE_EMAIL);
            return modelAndView;
        }

        userService.createUser(newUser);
        modelAndView.setViewName(Pages.EMPLOYEE_USER_LIST_PAGE);
        List<User> userList = userService.getUserList();
        modelAndView.addObject(Attributes.USER_LIST, userList);
        modelAndView.addObject(Attributes.HIGHLIGHT_SUCCESS_ID, newUser.getId());

        return modelAndView;
    }

    /**
     * Update user action.
     *
     * @param editedUser User object
     * @param result     Result of binding
     * @return View to redirect
     */
    @RequestMapping(value = Actions.EMPLOYEE_UPDATE_USER, method = RequestMethod.POST)
    public ModelAndView updateUser(@Valid @ModelAttribute(Attributes.EDITED_USER) User editedUser,
                                   BindingResult result) {

        User user = (User) httpSession.getAttribute(Attributes.USER);
        ModelAndView modelAndView = new ModelAndView(Pages.EMPLOYEE_USER_LIST_PAGE);
        modelAndView.addObject(Attributes.EDITED_USER_BIRTH_DATE, dateFormat.format(editedUser.getBirthDate()));

        if (result.hasErrors()) {
            List<User> userList = userService.getUserList();
            modelAndView.addObject(Attributes.USER_LIST, userList);
            return modelAndView;
        }

        //Checks whether email and passport data are unique
        User sameEmailUser = userService.getUserByEmail(editedUser.getEmail());
        User samePassportDataUser = userService.getUserByPassportData(editedUser.getPassportData());

        if (sameEmailUser != null || samePassportDataUser != null) {

            if (samePassportDataUser != null && samePassportDataUser.getId() != editedUser.getId()) {
                List<User> userList = userService.getUserList();
                modelAndView.addObject(Attributes.USER_LIST, userList);
                modelAndView.addObject(Attributes.NOT_UNIQUE_PASSPORT_DATA, Attributes.NOT_UNIQUE_PASSPORT_DATA);
                return modelAndView;
            }

            if (sameEmailUser != null && sameEmailUser.getId() != editedUser.getId()) {
                List<User> userList = userService.getUserList();
                modelAndView.addObject(Attributes.USER_LIST, userList);
                modelAndView.addObject(Attributes.NOT_UNIQUE_EMAIL, Attributes.NOT_UNIQUE_EMAIL);
                return modelAndView;
            }

        }

        userService.updateUser(user, editedUser);
        List<User> userList = userService.getUserList();
        modelAndView.addObject(Attributes.USER_LIST, userList);
        modelAndView.addObject(Attributes.EDITED_USER_BIRTH_DATE, null);
        modelAndView.addObject(Attributes.EDITED_USER, null);
        modelAndView.addObject(Attributes.HIGHLIGHT_SUCCESS_ID, editedUser.getId());

        return modelAndView;
    }

    /**
     * Delete user action.
     *
     * @param idString Id of user
     * @return View to redirect
     */
    @RequestMapping(value = Actions.EMPLOYEE_DELETE_USER, method = RequestMethod.POST)
    public String deleteUser(@RequestParam(Parameters.ID) String idString, Model model) {

        User user = (User) httpSession.getAttribute(Attributes.USER);

        userService.deleteUserById(user, idString);

        List<User> userList = userService.getUserList();
        model.addAttribute(Attributes.USER_LIST, userList);

        return Pages.EMPLOYEE_USER_LIST_PAGE;
    }

}
