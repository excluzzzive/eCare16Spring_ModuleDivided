package ru.simflex.ex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.simflex.ex.entities.Contract;
import ru.simflex.ex.entities.Option;
import ru.simflex.ex.entities.Tariff;
import ru.simflex.ex.services.interfaces.OptionService;
import ru.simflex.ex.util.constants.Actions;
import ru.simflex.ex.util.constants.Attributes;
import ru.simflex.ex.util.constants.Pages;
import ru.simflex.ex.util.constants.Parameters;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * Option controller class.
 */

@Controller
public class OptionController {

    /**
     * OptionService instance.
     */
    @Autowired
    private OptionService optionService;

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
        binder.addCustomFormatter(optionFormatter, Parameters.INCOMPATIBLE_OPTIONS, Parameters.JOINT_OPTIONS);
    }

    /**
     * Method showing page with list of options.
     *
     * @param model Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.SHOW_EMPLOYEE_OPTION_LIST_PAGE)
    public String showOptionListPage(Model model) {

        List<Option> optionList = optionService.getOptionList();

        model.addAttribute(Attributes.OPTION_LIST, optionList);
        return Pages.EMPLOYEE_OPTION_LIST_PAGE;
    }

    /**
     * Method showing page with create option form.
     *
     * @return View to redirect
     */
    @RequestMapping(value = Actions.SHOW_EMPLOYEE_OPTION_CREATE_PAGE)
    public ModelAndView showCreateOptionPage() {

        ModelAndView modelAndView = new ModelAndView(Pages.EMPLOYEE_OPTION_CREATE_PAGE);

        List<Option> optionList = optionService.getOptionList();
        modelAndView.addObject(Attributes.OPTION_LIST, optionList);

        return modelAndView;
    }


    /**
     * Methods showing page with edit option form
     *
     * @param idString Id of option
     * @param model    Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.SHOW_EMPLOYEE_EDIT_OPTION_BY_ID_PAGE, method = RequestMethod.POST)
    public String editOptionById(@RequestParam(Parameters.EDITED_OPTION_ID) String idString, Model model) {

        Option editedOption = optionService.getOptionById(idString);
        List<Option> optionList = optionService.getOptionList();

        List<Tariff> optionUsedByTariffList = optionService.getOptionUsedByTariffList(idString);
        List<Contract> optionUsedByContractList = optionService.getOptionUsedByContractList(idString);
        List<Option> optionUsedAsJointOptionList = optionService.getOptionUsedAsJointOptionList(idString);

        model.addAttribute(Attributes.OPTION_LIST, optionList);
        model.addAttribute(Attributes.EDITED_OPTION, editedOption);
        model.addAttribute(Attributes.OPTION_USED_BY_TARIFF_LIST, optionUsedByTariffList);
        model.addAttribute(Attributes.OPTION_USED_BY_CONTRACT_LIST, optionUsedByContractList);
        model.addAttribute(Attributes.OPTION_USED_AS_JOINT_OPTION_LIST, optionUsedAsJointOptionList);

        return Pages.EMPLOYEE_OPTION_LIST_PAGE;
    }

    /**
     * Create option action.
     *
     * @param newOption Option object by spring
     * @param result    Result list of binding
     * @return View to redirect
     */
    @RequestMapping(value = Actions.EMPLOYEE_CREATE_OPTION, method = RequestMethod.POST)
    public ModelAndView createOption(@Valid @ModelAttribute(Attributes.NEW_OPTION) Option newOption,
                                     BindingResult result) {

        ModelAndView modelAndView = new ModelAndView(Pages.EMPLOYEE_OPTION_CREATE_PAGE);

        if (result.hasErrors()) {
            List<Option> optionList = optionService.getOptionList();
            modelAndView.addObject(Attributes.OPTION_LIST, optionList);
            modelAndView.addObject(Attributes.NEW_OPTION, newOption);
            return modelAndView;
        }

        //Unique option name validation
        Option sameOption = optionService.getOptionByName(newOption.getName());
        if (sameOption != null) {

            List<Option> optionList = optionService.getOptionList();

            modelAndView.addObject(Attributes.NEW_OPTION, newOption);
            modelAndView.addObject(Attributes.OPTION_LIST, optionList);
            modelAndView.addObject(Attributes.NOT_UNIQUE_OPTION_NAME, Attributes.NOT_UNIQUE_OPTION_NAME);

            return modelAndView;
        }

        optionService.createOption(newOption);

        modelAndView.setViewName(Pages.EMPLOYEE_OPTION_LIST_PAGE);
        List<Option> optionList = optionService.getOptionList();
        modelAndView.addObject(Attributes.OPTION_LIST, optionList);
        modelAndView.addObject(Attributes.HIGHLIGHT_SUCCESS_ID, newOption.getId());

        return modelAndView;
    }

    /**
     * Update option action.
     *
     * @param editedOption Spring option model
     * @param result Result of binding
     * @return View to redirect
     */
    @RequestMapping(value = Actions.EMPLOYEE_UPDATE_OPTION, method = RequestMethod.POST)
    public ModelAndView updateOption(@Valid @ModelAttribute(Attributes.EDITED_OPTION) Option editedOption,
                                     BindingResult result) {

        ModelAndView modelAndView = new ModelAndView(Pages.EMPLOYEE_OPTION_LIST_PAGE);

        if (result.hasErrors()) {
            List<Option> optionList = optionService.getOptionList();
            modelAndView.addObject(Attributes.OPTION_LIST, optionList);
            modelAndView.addObject(Attributes.EDITED_OPTION, editedOption);
            return modelAndView;
        }

        //Unique option name validation
        Option sameOption = optionService.getOptionByName(editedOption.getName());
        if (sameOption != null && sameOption.getId() != editedOption.getId()) {

            String idString = String.valueOf(editedOption.getId());

            List<Option> optionList = optionService.getOptionList();
            List<Tariff> optionUsedByTariffList = optionService.getOptionUsedByTariffList(idString);
            List<Contract> optionUsedByContractList = optionService.getOptionUsedByContractList(idString);
            List<Option> optionUsedAsJointOptionList = optionService.getOptionUsedAsJointOptionList(idString);

            modelAndView.addObject(Attributes.EDITED_OPTION, editedOption);
            modelAndView.addObject(Attributes.OPTION_LIST, optionList);
            modelAndView.addObject(Attributes.OPTION_USED_BY_TARIFF_LIST, optionUsedByTariffList);
            modelAndView.addObject(Attributes.OPTION_USED_BY_CONTRACT_LIST, optionUsedByContractList);
            modelAndView.addObject(Attributes.OPTION_USED_AS_JOINT_OPTION_LIST, optionUsedAsJointOptionList);
            modelAndView.addObject(Attributes.NOT_UNIQUE_OPTION_NAME, Attributes.NOT_UNIQUE_OPTION_NAME);

            return modelAndView;
        }

        optionService.updateOption(editedOption);

        List<Option> optionList = optionService.getOptionList();
        modelAndView.addObject(Attributes.OPTION_LIST, optionList);
        modelAndView.addObject(Attributes.EDITED_OPTION, null);
        modelAndView.addObject(Attributes.HIGHLIGHT_SUCCESS_ID, editedOption.getId());

        return modelAndView;

    }

    /**
     * Delete option action.
     *
     * @param idString Id of option
     * @return View to redirect
     */
    @RequestMapping(value = Actions.EMPLOYEE_DELETE_OPTION, method = RequestMethod.POST)
    public String deleteOption(@RequestParam(Parameters.ID) String idString, Model model) {

        optionService.deleteOptionById(idString);
        List<Option> optionList = optionService.getOptionList();
        model.addAttribute(Attributes.OPTION_LIST, optionList);

        return Pages.EMPLOYEE_OPTION_LIST_PAGE;
    }

}
