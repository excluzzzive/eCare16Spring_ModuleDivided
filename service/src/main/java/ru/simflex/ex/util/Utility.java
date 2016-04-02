package ru.simflex.ex.util;

import java.io.File;
import java.io.IOException;

/**
 * Utility class
 */
public class Utility {

    /**
     * Parses string money amount into Double object.
     * @param priceString Text form of money amount
     * @return Double - money amount
     */
    public static double parsePrice(String priceString) {
        double price;
        try {
            price = Double.parseDouble(priceString);
            if (price < 0) {
                throw new IllegalArgumentException("Negative price!");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Incorrect Price data.", e);
        }
        return price;
    }

    /**
     * Method counts number of pages for pagination by summ and limit.
     * @param entityCount Quantity of entities
     * @param limitPerPage Limit of objects per page
     * @return Quantity of pages
     */
    public static int getTotalPages(long entityCount, int limitPerPage) {
        if (entityCount % limitPerPage == 0) {
            return (int) entityCount / limitPerPage;
        } else {
            return (int) entityCount / limitPerPage + 1;
        }
    }

    /**
     * Restrict constructor.
     */
    private Utility() {
    }
}
