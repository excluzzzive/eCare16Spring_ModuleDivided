package ru.simflex.ex.webservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.simflex.ex.services.interfaces.ContractService;
import ru.simflex.ex.webservices.entities.WSUser;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * User web services.
 */
@Component
@Path(value = "/user")
public class UserWebService {

    @Autowired
    private ContractService contractService;

    @GET
    @Path("/list/{tariffId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<WSUser> getUserListByTariffId(@PathParam("tariffId") String idString) {
        return contractService.getWSUserListByTariffId(idString);
    }

}
