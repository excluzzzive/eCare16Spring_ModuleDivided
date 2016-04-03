package ru.simflex.ex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.simflex.ex.entities.PhoneNumber;
import ru.simflex.ex.exceptions.PhoneNumberCreatingException;
import ru.simflex.ex.services.interfaces.PhoneNumberService;
import ru.simflex.ex.constants.*;
import ru.simflex.ex.util.Utility;

import javax.servlet.http.HttpSession;
import java.util.List;

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
     *
     * @param model Model object
     * @return View to redirect
     */
    @RequestMapping(value = "/showEmployeePhoneNumberListPage")
    public String showPhoneNumberListPage(@RequestParam(value = "page") Integer page, Model model) {

        long phoneNumberCount = phoneNumberService.getPhoneNumberCount();
        int totalPages = Utility.getTotalPages(phoneNumberCount, Attributes.LIMIT_PHONE_NUMBERS_PER_PAGE);

        if (page > totalPages) {
            page = totalPages;
        }

        List<PhoneNumber> phoneNumberList = phoneNumberService.getPhoneNumberListByPage(page - 1);

        int beginIndex = Math.max(1, page - 5);
        int endIndex = Math.min(beginIndex + 10, totalPages);

        model.addAttribute("currentIndex", page);
        model.addAttribute("beginIndex", beginIndex);
        model.addAttribute("endIndex", endIndex);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute(Attributes.PHONE_NUMBER_LIST, phoneNumberList);

        return Pages.EMPLOYEE_PHONE_NUMBER_LIST_PAGE;
    }

    /**
     * Create PhoneNumber object action.
     *
     * @param newPhoneNumber String form of phone number
     * @param model          Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.EMPLOYEE_CREATE_PHONE_NUMBER, method = RequestMethod.POST)
    public String addPhoneNumber(@RequestParam(Parameters.NEW_PHONE_NUMBER) String newPhoneNumber,
                                 @RequestParam(value = "currentIndex") Integer page, Model model) {

        //Unique phone number validation
        PhoneNumber samePhoneNumber = phoneNumberService.getPhoneNumberByString(newPhoneNumber);
        if (samePhoneNumber != null) {
            model.addAttribute(Attributes.NOT_UNIQUE_PHONE_NUMBER, Attributes.NOT_UNIQUE_PHONE_NUMBER);
            return showPhoneNumberListPage(page, model);
        }

        try {
            phoneNumberService.addPhoneNumber(newPhoneNumber);
            page = phoneNumberService.getCurrentPageByPhoneNumberString(newPhoneNumber);
        } catch (PhoneNumberCreatingException e) {
            model.addAttribute(Attributes.PHONE_NUMBER_INVALID, true);
        }

        model.addAttribute(Attributes.HIGHLIGHT_SUCCESS_PHONE_NUMBER, newPhoneNumber);
        return showPhoneNumberListPage(page, model);
    }

    /**
     * Delete PhoneNumber object action
     *
     * @param deletedPhoneNumberIdString Id of phoneNumber
     * @param model                      Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.EMPLOYEE_DELETE_PHONE_NUMBER, method = RequestMethod.POST)
    public String deletePhoneNumber(@RequestParam(Parameters.ID) String deletedPhoneNumberIdString,
                                    @RequestParam(value = "currentIndex") Integer page, Model model) {

        int deletedPhoneNumberId = Integer.parseInt(deletedPhoneNumberIdString);
        phoneNumberService.deletePhoneNumberById(deletedPhoneNumberId);

        return showPhoneNumberListPage(page, model);
    }
}
