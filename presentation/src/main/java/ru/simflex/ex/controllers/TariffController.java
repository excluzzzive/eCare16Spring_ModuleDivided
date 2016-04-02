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
import ru.simflex.ex.services.interfaces.ContractService;
import ru.simflex.ex.services.interfaces.OptionService;
import ru.simflex.ex.services.interfaces.TariffService;
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
public class TariffController {

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
     * Option formatter to resolve id of options into Option object.
     */
    @Autowired
    private Formatter optionFormatter;

    /**
     * {@inheritDoc}
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addCustomFormatter(optionFormatter, Parameters.POSSIBLE_OPTIONS);
    }

    /**
     * Method showing page with list of tariffs.
     *
     * @param model Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.SHOW_EMPLOYEE_TARIFF_LIST_PAGE)
    public String showTariffListPage(Model model) {

        List<Tariff> tariffList = tariffService.getTariffList();
        model.addAttribute(Attributes.TARIFF_LIST, tariffList);

        return Pages.EMPLOYEE_TARIFF_LIST_PAGE;
    }

    /**
     * Method showing page with create tariff form.
     *
     * @return View to redirect
     */
    @RequestMapping(value = Actions.SHOW_EMPLOYEE_TARIFF_CREATE_PAGE)
    public ModelAndView showCreateTariffPage() {


        ModelAndView modelAndView = new ModelAndView(Pages.EMPLOYEE_TARIFF_CREATE_PAGE);

        List<Option> optionList = optionService.getOptionList();
        modelAndView.addObject(Attributes.OPTION_LIST, optionList);

        return modelAndView;
    }


    /**
     * Methods showing page with edit tariff form
     *
     * @param idString Id of tariff
     * @param model    Model object
     * @return View to redirect
     */
    @RequestMapping(value = Actions.SHOW_EMPLOYEE_EDIT_TARIFF_BY_ID_PAGE, method = RequestMethod.POST)
    public String editTariffById(@RequestParam(Parameters.ID) String idString, Model model) {

        Tariff editedTariff = tariffService.getTariffById(idString);
        List<Option> optionList = optionService.getOptionList();
        List<Contract> contractListByTariff = contractService.getContractListByTariffId(idString);
        List<Tariff> tariffList = tariffService.getTariffList();

        model.addAttribute(Attributes.TARIFF_LIST, tariffList);
        model.addAttribute(Attributes.OPTION_LIST, optionList);
        model.addAttribute(Attributes.CONTRACT_LIST_BY_TARIFF, contractListByTariff);
        model.addAttribute(Attributes.EDITED_TARIFF, editedTariff);

        return Pages.EMPLOYEE_TARIFF_LIST_PAGE;
    }

    /**
     * Create tariff action.
     *
     * @return View to redirect
     */
    @RequestMapping(value = Actions.EMPLOYEE_CREATE_TARIFF, method = RequestMethod.POST)
    public ModelAndView createTariff(@Valid @ModelAttribute(Attributes.NEW_TARIFF) Tariff newTariff,
                                     BindingResult result) {

        ModelAndView modelAndView = new ModelAndView(Pages.EMPLOYEE_TARIFF_CREATE_PAGE);


        if (result.hasErrors()) {
            List<Option> optionList = optionService.getOptionList();
            modelAndView.addObject(Attributes.OPTION_LIST, optionList);
            modelAndView.addObject(Attributes.NEW_TARIFF, newTariff);
            return modelAndView;
        }

        //My tariff unique name validator
        Tariff sameTariff = tariffService.getTariffByName(newTariff.getName());
        if (sameTariff != null) {

            List<Option> optionList = optionService.getOptionList();
            modelAndView.addObject(Attributes.OPTION_LIST, optionList);
            modelAndView.addObject(Attributes.NEW_TARIFF, newTariff);
            modelAndView.addObject(Attributes.NOT_UNIQUE_TARIFF_NAME, Attributes.NOT_UNIQUE_TARIFF_NAME);
            return modelAndView;
        }

        tariffService.createTariff(newTariff);

        List<Tariff> tariffList = tariffService.getTariffList();
        modelAndView.addObject(Attributes.TARIFF_LIST, tariffList);
        modelAndView.addObject(Attributes.HIGHLIGHT_SUCCESS_ID, newTariff.getId());

        modelAndView.setViewName(Pages.EMPLOYEE_TARIFF_LIST_PAGE);

        return modelAndView;

    }

    /**
     * Update tariff action.
     *
     * @return View to redirect
     */
    @RequestMapping(value = Actions.EMPLOYEE_UPDATE_TARIFF, method = RequestMethod.POST)
    public ModelAndView updateTariff(@Valid @ModelAttribute(Attributes.EDITED_TARIFF) Tariff editedTariff,
                                     BindingResult result) {

        ModelAndView modelAndView = new ModelAndView(Pages.EMPLOYEE_TARIFF_LIST_PAGE);

        if (result.hasErrors()) {
            List<Option> optionList = optionService.getOptionList();
            List<Contract> contractListByTariff = contractService.
                    getContractListByTariffId(String.valueOf(editedTariff.getId()));
            List<Tariff> tariffList = tariffService.getTariffList();

            modelAndView.addObject(Attributes.TARIFF_LIST, tariffList);
            modelAndView.addObject(Attributes.CONTRACT_LIST_BY_TARIFF, contractListByTariff);
            modelAndView.addObject(Attributes.OPTION_LIST, optionList);
            modelAndView.addObject(Attributes.EDITED_TARIFF, editedTariff);
            return modelAndView;
        }

        //My tariff unique name validator
        Tariff sameTariff = tariffService.getTariffByName(editedTariff.getName());
        if (sameTariff != null && sameTariff.getId() != editedTariff.getId()) {

            List<Option> optionList = optionService.getOptionList();
            List<Contract> contractListByTariff = contractService.
                    getContractListByTariffId(String.valueOf(editedTariff.getId()));
            List<Tariff> tariffList = tariffService.getTariffList();

            modelAndView.addObject(Attributes.TARIFF_LIST, tariffList);
            modelAndView.addObject(Attributes.CONTRACT_LIST_BY_TARIFF, contractListByTariff);
            modelAndView.addObject(Attributes.OPTION_LIST, optionList);
            modelAndView.addObject(Attributes.EDITED_TARIFF, editedTariff);
            modelAndView.addObject(Attributes.NOT_UNIQUE_TARIFF_NAME, Attributes.NOT_UNIQUE_TARIFF_NAME);
            return modelAndView;
        }

        tariffService.updateTariff(editedTariff);
        List<Tariff> tariffList = tariffService.getTariffList();

        modelAndView.addObject(Attributes.TARIFF_LIST, tariffList);
        modelAndView.addObject(Attributes.EDITED_TARIFF, null);
        modelAndView.addObject(Attributes.HIGHLIGHT_SUCCESS_ID, editedTariff.getId());

        return modelAndView;
    }

    /**
     * Delete tariff action.
     *
     * @param idString Id of tariff
     * @return View to redirect
     */
    @RequestMapping(value = Actions.EMPLOYEE_DELETE_TARIFF, method = RequestMethod.POST)
    public String deleteTariff(@RequestParam(Parameters.ID) String idString, Model model) {

        tariffService.deleteTariffById(idString);

        List<Tariff> tariffList = tariffService.getTariffList();
        model.addAttribute(Attributes.TARIFF_LIST, tariffList);

        return Pages.EMPLOYEE_TARIFF_LIST_PAGE;
    }

}
