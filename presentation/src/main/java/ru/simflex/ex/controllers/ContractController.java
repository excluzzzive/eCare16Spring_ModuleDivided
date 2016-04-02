package ru.simflex.ex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.simflex.ex.entities.*;
import ru.simflex.ex.exceptions.ContractCreatingException;
import ru.simflex.ex.services.interfaces.*;
import ru.simflex.ex.constants.Actions;
import ru.simflex.ex.constants.Attributes;
import ru.simflex.ex.constants.Pages;
import ru.simflex.ex.constants.Parameters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

/**
 * Option controller class.
 */
@Controller
public class ContractController {

    /**
     * ContractService instance.
     */
    @Autowired
    private ContractService contractService;

    /**
     * OptionService instance.
     */
    @Autowired
    private OptionService optionService;

    /**
     * PhoneNumberService instance.
     */
    @Autowired
    private PhoneNumberService phoneNumberService;

    /**
     * TariffService instance.
     */
    @Autowired
    private TariffService tariffService;

    /**
     * UserService instance.
     */
    @Autowired
    private UserService userService;

    /**
     * HttpSession instance.
     */
    @Autowired
    private HttpSession httpSession;

    /**
     * Option formatter to resolve id of options into Option object.
     */
    @Autowired
    private Formatter optionFormatter;

    /**
     * {@inheritDoc}
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addCustomFormatter(optionFormatter, Parameters.CHOSEN_OPTIONS);
    }

    /**
     * Method showing page with list of contracts.
     *
     * @param model Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.SHOW_USER_CONTRACT_LIST_PAGE)
    public String showUserContractListPage(Model model) {

        User user = (User) httpSession.getAttribute(Attributes.USER);

        List<Contract> contractList = contractService.getContractListByUser(user);
        model.addAttribute(Attributes.CONTRACT_LIST, contractList);

        return Pages.USER_CONTRACT_LIST_PAGE;
    }

    /**
     * Method showing page with edit contract form.
     *
     * @param idString Id of option
     * @param model    Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.SHOW_USER_EDIT_CONTRACT_BY_ID_PAGE, method = RequestMethod.POST)
    public String editUserContractById(@RequestParam(Parameters.ID) String idString,
                                   HttpServletRequest request, Model model) {

        User user = (User) httpSession.getAttribute(Attributes.USER);

        //Check maybe it is a block contract action
        String btnBlock = request.getParameter(Parameters.BUTTON_BLOCK);
        if (btnBlock != null && !btnBlock.isEmpty()) {

            contractService.changeBlockState(null, idString);
            List<Contract> contractList = contractService.getContractListByUser(user);
            model.addAttribute(Attributes.CONTRACT_LIST, contractList);

            return Pages.USER_CONTRACT_LIST_PAGE;
        }

        contractService.checkContractBlocked(idString);

        List<Contract> contractList = contractService.getContractListByUser(user);
        Contract editedContract = contractService.getContractByUserAndId(user, idString);
        List<Tariff> tariffList = tariffService.getTariffList();

        model.addAttribute(Attributes.CONTRACT_LIST, contractList);
        model.addAttribute(Attributes.EDITED_CONTRACT, editedContract);
        model.addAttribute(Attributes.TARIFF_LIST, tariffList);

        return Pages.USER_CONTRACT_LIST_PAGE;
    }

    /**
     * Update contract action.
     *
     * @return View to redirect
     */
    @RequestMapping(value = Actions.USER_UPDATE_CONTRACT, method = RequestMethod.POST)
    public ModelAndView userUpdateContract(@RequestParam(Parameters.ID) String idString,
                                       @RequestParam(Parameters.TARIFF_ID) String tariffIdString,
                                       HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView(Pages.USER_CONTRACT_LIST_PAGE);

        User user = (User) httpSession.getAttribute(Attributes.USER);

        String[] chosenOptionArray = request.getParameterValues(tariffIdString + "_"
                + Parameters.CHOSEN_OPTIONS);

        List<Option> chosenOptionList = optionService.optionIdsArrayToListOfOption(chosenOptionArray);
        Set<Option> chosenIncompatibleOptionSet = optionService.checkIncompatibleOptions(chosenOptionList);
        Set<Option> chosenJointOptionSet = optionService.checkJointOptions(chosenOptionList);

        Contract editedContract = contractService.getContractByUserAndId(user, idString);
        Tariff selectedTariff = tariffService.getTariffById(tariffIdString);
        editedContract.setTariff(selectedTariff);
        editedContract.setChosenOptions(chosenOptionList);

        //Incompatible and Joint options validation
        boolean cannotUpdate = false;
        if (chosenIncompatibleOptionSet != null && !chosenIncompatibleOptionSet.isEmpty()) {
            cannotUpdate = true;
            modelAndView.addObject(Attributes.INCOMPATIBLE_OPTIONS_SET, chosenIncompatibleOptionSet);
        }

        if (chosenJointOptionSet != null && !chosenJointOptionSet.isEmpty()) {
            cannotUpdate = true;
            modelAndView.addObject(Attributes.JOINT_OPTIONS_SET, chosenJointOptionSet);
        }

        if (cannotUpdate) {
            List<Contract> contractList = contractService.getContractListByUser(user);
            List<Tariff> tariffList = tariffService.getTariffList();

            modelAndView.addObject(Attributes.EDITED_CONTRACT, editedContract);
            modelAndView.addObject(Attributes.CONTRACT_LIST, contractList);
            modelAndView.addObject(Attributes.TARIFF_LIST, tariffList);

            return modelAndView;
        }


        contractService.updateContract(editedContract);
        List<Contract> contractList = contractService.getContractListByUser(user);
        modelAndView.addObject(Attributes.CONTRACT_LIST, contractList);
        modelAndView.addObject(Attributes.HIGHLIGHT_SUCCESS_ID, editedContract.getId());

        return modelAndView;
    }

    /**
     * Method showing page with list of contracts.
     *
     * @param model Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.SHOW_EMPLOYEE_CONTRACT_LIST_PAGE)
    public String showEmployeeContractListPage(Model model) {

        List<Contract> contractList = contractService.getContractList();
        model.addAttribute(Attributes.CONTRACT_LIST, contractList);

        return Pages.EMPLOYEE_CONTRACT_LIST_PAGE;
    }

    /**
     * Method showing page with create contract form.
     *
     * @return View to redirect
     */
    @RequestMapping(value = Actions.SHOW_EMPLOYEE_CONTRACT_CREATE_PAGE)
    public ModelAndView showCreateContractPage(@RequestParam(Attributes.CURRENT_STEP) String currentStep,
                                               HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView(Pages.EMPLOYEE_CONTRACT_CREATE_PAGE);

        if (currentStep.equals(Attributes.STEP_ZERO)) {
            Contract newContract = new Contract();
            httpSession.setAttribute(Attributes.NEW_CONTRACT, newContract);

            List<User> userList = userService.getUserList();

            modelAndView.addObject(Attributes.CURRENT_STEP, Attributes.STEP_ONE);
            modelAndView.addObject(Attributes.USER_LIST, userList);

            return modelAndView;
        }

        //Choose User
        if (currentStep.equals(Attributes.STEP_ONE)) {
            Contract newContract = (Contract) httpSession.getAttribute(Attributes.NEW_CONTRACT);

            String userId = request.getParameter(Parameters.USER_ID);
            if (userId != null && !userId.isEmpty()) {
                User newContractUser = userService.getUserById(userId);
                newContract.setUser(newContractUser);
            } else {
                List<User> userList = userService.getUserList();
                modelAndView.addObject(Attributes.USER_LIST, userList);
                modelAndView.addObject(Attributes.USER_NOT_SELECTED, true);
                modelAndView.addObject(Attributes.CURRENT_STEP, Attributes.STEP_ONE);
                return modelAndView;
            }

            List<PhoneNumber> phoneNumberList = phoneNumberService.getAvailablePhoneNumberList();

            modelAndView.addObject(Attributes.CURRENT_STEP, Attributes.STEP_TWO);
            modelAndView.addObject(Attributes.PHONE_NUMBER_LIST, phoneNumberList);

            return modelAndView;
        }

        //Choose PhoneNumber
        if (currentStep.equals(Attributes.STEP_TWO)) {

            String btnBack = request.getParameter(Parameters.BUTTON_BACK);
            if (btnBack != null && !btnBack.isEmpty()) {
                List<User> userList = userService.getUserList();
                modelAndView.addObject(Attributes.USER_LIST, userList);
                modelAndView.addObject(Attributes.CURRENT_STEP, Attributes.STEP_ONE);
                return modelAndView;
            }

            Contract newContract = (Contract) httpSession.getAttribute(Attributes.NEW_CONTRACT);

            String phoneNumberId = request.getParameter(Parameters.PHONE_NUMBER_ID);
            if (phoneNumberId != null && !phoneNumberId.isEmpty()) {
                PhoneNumber newPhoneNumber = phoneNumberService.getPhoneNumberById(phoneNumberId);
                if (newPhoneNumber == null || !newPhoneNumber.isAvailable()) {
                    List<PhoneNumber> phoneNumberList = phoneNumberService.getAvailablePhoneNumberList();
                    modelAndView.addObject(Attributes.PHONE_NUMBER_LIST, phoneNumberList);
                    modelAndView.addObject(Attributes.NOT_AVAILABLE_PHONE_NUMBER, Attributes.NOT_AVAILABLE_PHONE_NUMBER);
                    modelAndView.addObject(Attributes.CURRENT_STEP, Attributes.STEP_TWO);
                    return modelAndView;
                }
                newContract.setPhoneNumber(newPhoneNumber);
            } else {
                List<PhoneNumber> phoneNumberList = phoneNumberService.getAvailablePhoneNumberList();
                modelAndView.addObject(Attributes.PHONE_NUMBER_LIST, phoneNumberList);
                modelAndView.addObject(Attributes.PHONE_NUMBER_NOT_SELECTED, true);
                modelAndView.addObject(Attributes.CURRENT_STEP, Attributes.STEP_TWO);
                return modelAndView;
            }

            List<Tariff> tariffList = tariffService.getTariffList();

            modelAndView.addObject(Attributes.CURRENT_STEP, Attributes.STEP_THREE);
            modelAndView.addObject(Attributes.TARIFF_LIST, tariffList);

            return modelAndView;
        }

        //Choose Tariff and Options
        if (currentStep.equals(Attributes.STEP_THREE)) {

            String btnBack = request.getParameter(Parameters.BUTTON_BACK);
            if (btnBack != null && !btnBack.isEmpty()) {
                List<PhoneNumber> phoneNumberList = phoneNumberService.getAvailablePhoneNumberList();
                modelAndView.addObject(Attributes.PHONE_NUMBER_LIST, phoneNumberList);
                modelAndView.addObject(Attributes.CURRENT_STEP, Attributes.STEP_TWO);
                return modelAndView;
            }

            Contract newContract = (Contract) httpSession.getAttribute(Attributes.NEW_CONTRACT);

            String tariffId = request.getParameter(Parameters.TARIFF_ID);
            if (tariffId != null && !tariffId.isEmpty()) {
                String[] chosenOptionArray = request.getParameterValues(tariffId + "_"
                        + Parameters.CHOSEN_OPTIONS);

                List<Option> chosenOptionList = optionService.optionIdsArrayToListOfOption(chosenOptionArray);
                Set<Option> chosenIncompatibleOptionSet = optionService.checkIncompatibleOptions(chosenOptionList);
                Set<Option> chosenJointOptionSet = optionService.checkJointOptions(chosenOptionList);

                Tariff newContractTariff = tariffService.getTariffById(tariffId);
                newContract.setTariff(newContractTariff);
                newContract.setChosenOptions(chosenOptionList);

                modelAndView.addObject(Attributes.NEW_CONTRACT, newContract);

                //Incompatible and Joint options validation
                boolean cannotUpdate = false;
                if (chosenIncompatibleOptionSet != null && !chosenIncompatibleOptionSet.isEmpty()) {
                    cannotUpdate = true;
                    modelAndView.addObject(Attributes.INCOMPATIBLE_OPTIONS_SET, chosenIncompatibleOptionSet);
                }

                if (chosenJointOptionSet != null && !chosenJointOptionSet.isEmpty()) {
                    cannotUpdate = true;
                    modelAndView.addObject(Attributes.JOINT_OPTIONS_SET, chosenJointOptionSet);
                }

                if (cannotUpdate) {
                    List<Tariff> tariffList = tariffService.getTariffList();
                    modelAndView.addObject(Attributes.CURRENT_STEP, Attributes.STEP_THREE);
                    modelAndView.addObject(Attributes.TARIFF_LIST, tariffList);
                    return modelAndView;
                }
            } else {
                List<Tariff> tariffList = tariffService.getTariffList();
                modelAndView.addObject(Attributes.TARIFF_LIST, tariffList);
                modelAndView.addObject(Attributes.TARIFF_NOT_SELECTED, true);
                modelAndView.addObject(Attributes.CURRENT_STEP, Attributes.STEP_THREE);
                return modelAndView;
            }

            if (newContract.getUser() == null || newContract.getTariff() == null
                    || newContract.getPhoneNumber() == null) {
                modelAndView.addObject(Attributes.CAN_FINISH, false);
            }

            modelAndView.addObject(Attributes.CURRENT_STEP, Attributes.STEP_SUMMARY);
            return modelAndView;
        }

        //Summary
        if (currentStep.equals(Attributes.STEP_SUMMARY)) {

            String btnBack = request.getParameter(Parameters.BUTTON_BACK);
            if (btnBack != null && !btnBack.isEmpty()) {
                List<Tariff> tariffList = tariffService.getTariffList();
                modelAndView.addObject(Attributes.CURRENT_STEP, Attributes.STEP_THREE);
                modelAndView.addObject(Attributes.TARIFF_LIST, tariffList);
                return modelAndView;
            }

            Contract newContract = (Contract) httpSession.getAttribute(Attributes.NEW_CONTRACT);
            if (newContract.getUser() != null && newContract.getTariff() != null
                    && newContract.getPhoneNumber() != null) {
                try {
                    contractService.createContract(newContract);
                } catch (ContractCreatingException e) {
                    modelAndView.addObject(Attributes.CURRENT_STEP, Attributes.STEP_SUMMARY);
                    modelAndView.addObject(Attributes.EXCEPTION, e);

                    return modelAndView;
                }
                modelAndView.addObject(Attributes.HIGHLIGHT_SUCCESS_ID, newContract.getId());
            } else {
                modelAndView.addObject(Attributes.CURRENT_STEP, Attributes.STEP_SUMMARY);
                return modelAndView;
            }
        }

        List<Contract> contractList = contractService.getContractList();
        modelAndView.addObject(Attributes.CONTRACT_LIST, contractList);
        modelAndView.setViewName(Pages.EMPLOYEE_CONTRACT_LIST_PAGE);
        return modelAndView;
    }


    /**
     * Methods showing page with edit contract form.
     *
     * @param idString Id of option
     * @param model    Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.SHOW_EMPLOYEE_EDIT_CONTRACT_BY_ID_PAGE, method = RequestMethod.POST)
    public String editContractById(@RequestParam(Parameters.ID) String idString,
                                   HttpServletRequest request, Model model) {

        User user = (User) httpSession.getAttribute(Attributes.USER);

        //Check maybe it is a block contract action
        String btnBlock = request.getParameter(Parameters.BUTTON_BLOCK);
        if (btnBlock != null && !btnBlock.isEmpty()) {

            contractService.changeBlockState(user, idString);
            List<Contract> contractList = contractService.getContractList();
            model.addAttribute(Attributes.CONTRACT_LIST, contractList);

            return Pages.EMPLOYEE_CONTRACT_LIST_PAGE;
        }

        contractService.checkContractBlocked(idString);

        List<Contract> contractList = contractService.getContractList();
        Contract editedContract = contractService.getContractById(idString);
        List<Tariff> tariffList = tariffService.getTariffList();

        model.addAttribute(Attributes.CONTRACT_LIST, contractList);
        model.addAttribute(Attributes.EDITED_CONTRACT, editedContract);
        model.addAttribute(Attributes.TARIFF_LIST, tariffList);

        return Pages.EMPLOYEE_CONTRACT_LIST_PAGE;
    }

    /**
     * Update contract action.
     *
     * @return View to redirect
     */
    @RequestMapping(value = Actions.EMPLOYEE_UPDATE_CONTRACT, method = RequestMethod.POST)
    public ModelAndView employeeUpdateContract(@RequestParam(Parameters.ID) String idString,
                                       @RequestParam(Parameters.TARIFF_ID) String tariffIdString,
                                       HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView(Pages.EMPLOYEE_CONTRACT_LIST_PAGE);

        String[] chosenOptionArray = request.getParameterValues(tariffIdString + "_"
                + Parameters.CHOSEN_OPTIONS);

        List<Option> chosenOptionList = optionService.optionIdsArrayToListOfOption(chosenOptionArray);
        Set<Option> chosenIncompatibleOptionSet = optionService.checkIncompatibleOptions(chosenOptionList);
        Set<Option> chosenJointOptionSet = optionService.checkJointOptions(chosenOptionList);

        Contract editedContract = contractService.getContractById(idString);
        Tariff selectedTariff = tariffService.getTariffById(tariffIdString);
        editedContract.setTariff(selectedTariff);
        editedContract.setChosenOptions(chosenOptionList);

        //Incompatible and Joint options validation
        boolean cannotUpdate = false;
        if (chosenIncompatibleOptionSet != null && !chosenIncompatibleOptionSet.isEmpty()) {
            cannotUpdate = true;
            modelAndView.addObject(Attributes.INCOMPATIBLE_OPTIONS_SET, chosenIncompatibleOptionSet);
        }

        if (chosenJointOptionSet != null && !chosenJointOptionSet.isEmpty()) {
            cannotUpdate = true;
            modelAndView.addObject(Attributes.JOINT_OPTIONS_SET, chosenJointOptionSet);
        }

        if (cannotUpdate) {
            List<Contract> contractList = contractService.getContractList();
            List<Tariff> tariffList = tariffService.getTariffList();

            modelAndView.addObject(Attributes.EDITED_CONTRACT, editedContract);
            modelAndView.addObject(Attributes.CONTRACT_LIST, contractList);
            modelAndView.addObject(Attributes.TARIFF_LIST, tariffList);

            return modelAndView;
        }


        contractService.updateContract(editedContract);
        List<Contract> contractList = contractService.getContractList();
        modelAndView.addObject(Attributes.CONTRACT_LIST, contractList);
        modelAndView.addObject(Attributes.HIGHLIGHT_SUCCESS_ID, editedContract.getId());

        return modelAndView;
    }

    /**
     * Delete contract action.
     *
     * @param idString Id of option
     * @return View to redirect
     */
    @RequestMapping(value = Actions.EMPLOYEE_DELETE_CONTRACT, method = RequestMethod.POST)
    public String deleteContract(@RequestParam(Parameters.ID) String idString, Model model) {

        contractService.deleteContractById(idString);

        List<Contract> contractList = contractService.getContractList();
        model.addAttribute(Attributes.CONTRACT_LIST, contractList);

        return Pages.EMPLOYEE_CONTRACT_LIST_PAGE;
    }

}
