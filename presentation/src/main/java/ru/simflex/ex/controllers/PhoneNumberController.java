package ru.simflex.ex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.simflex.ex.entities.PhoneNumber;
import ru.simflex.ex.entities.User;
import ru.simflex.ex.exceptions.PhoneNumberCreatingException;
import ru.simflex.ex.services.interfaces.PhoneNumberService;
import ru.simflex.ex.services.interfaces.UserService;
import ru.simflex.ex.util.constants.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * PhoneNumber controller class.
 */

@Controller
public class PhoneNumberController {

    /**
     * PhoneNumberService instance.
     */
    @Autowired
    private PhoneNumberService phoneNumberService;

    /**
     * HttpSession instance.
     */
    @Autowired
    private HttpSession httpSession;

    /**
     * Shows employee phone number list page.
     * @param model Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.SHOW_EMPLOYEE_PHONE_NUMBER_LIST_PAGE)
    public String showPhoneNumberListPage(Model model) {

        List<PhoneNumber> phoneNumberList = phoneNumberService.getPhoneNumberList();
        model.addAttribute(Attributes.PHONE_NUMBER_LIST, phoneNumberList);

        return Pages.EMPLOYEE_PHONE_NUMBER_LIST_PAGE;
    }

    /**
     * Create PhoneNumber object action.
     * @param newPhoneNumber String form of phone number
     * @param model Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.EMPLOYEE_CREATE_PHONE_NUMBER, method = RequestMethod.POST)
    public String addPhoneNumber(@RequestParam(Parameters.NEW_PHONE_NUMBER) String newPhoneNumber, Model model) {

        //Unique phone number validation
        PhoneNumber samePhoneNumber = phoneNumberService.getPhoneNumberByString(newPhoneNumber);
        if (samePhoneNumber != null) {
            List<PhoneNumber> phoneNumberList = phoneNumberService.getPhoneNumberList();
            model.addAttribute(Attributes.PHONE_NUMBER_LIST, phoneNumberList);
            model.addAttribute(Attributes.NOT_UNIQUE_PHONE_NUMBER, Attributes.NOT_UNIQUE_PHONE_NUMBER);

            return Pages.EMPLOYEE_PHONE_NUMBER_LIST_PAGE;
        }

        try {
            phoneNumberService.addPhoneNumber(newPhoneNumber);
        } catch (PhoneNumberCreatingException e) {
            model.addAttribute(Attributes.PHONE_NUMBER_INVALID, true);
        }

        List<PhoneNumber> phoneNumberList = phoneNumberService.getPhoneNumberList();
        model.addAttribute(Attributes.PHONE_NUMBER_LIST, phoneNumberList);
        model.addAttribute(Attributes.HIGHLIGHT_SUCCESS_PHONE_NUMBER, newPhoneNumber);

        return Pages.EMPLOYEE_PHONE_NUMBER_LIST_PAGE;
    }

    /**
     * Delete PhoneNumber object action
     * @param deletedPhoneNumberIdString Id of phoneNumber
     * @param model Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.EMPLOYEE_DELETE_PHONE_NUMBER, method = RequestMethod.POST)
    public String deletePhoneNumber(@RequestParam(Parameters.ID) String deletedPhoneNumberIdString,
                                    Model model) {

        int deletedPhoneNumberId = Integer.parseInt(deletedPhoneNumberIdString);
        phoneNumberService.deletePhoneNumberById(deletedPhoneNumberId);

        List<PhoneNumber> phoneNumberList = phoneNumberService.getPhoneNumberList();
        model.addAttribute(Attributes.PHONE_NUMBER_LIST, phoneNumberList);

        return Pages.EMPLOYEE_PHONE_NUMBER_LIST_PAGE;
    }
}
