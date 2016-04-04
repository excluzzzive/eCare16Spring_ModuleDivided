package ru.simflex.ex.webservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.simflex.ex.services.interfaces.TariffService;
import ru.simflex.ex.webservices.entities.WSTariff;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Tariff web services.
 */
@Component
@Path(value = "/tariff")
public class TariffWebService {

    @Autowired
    private TariffService tariffService;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<WSTariff> getTariffList() {
        return tariffService.getWSTariffList();
    }
}
