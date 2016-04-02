package ru.simflex.ex.formatters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
import ru.simflex.ex.entities.Option;
import ru.simflex.ex.services.interfaces.OptionService;

import java.text.ParseException;
import java.util.Locale;

/**
 * Formatter for Spring Option binding.
 */
@Component
public class OptionFormatter implements Formatter<Option> {

    /**
     * OptionService instance.
     */
    @Autowired
    private OptionService optionService;

    /**
     * {@inheritDoc}
     */
    public Option parse(String text, Locale locale) throws ParseException {
        return optionService.getOptionById(text);
    }

    /**
     * {@inheritDoc}
     */
    public String print(Option object, Locale locale) {
        return String.valueOf(object.getId());
    }
}
