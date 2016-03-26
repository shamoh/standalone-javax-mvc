package cz.kramolis.tests.mvc;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mvc.Models;
import javax.mvc.annotation.Controller;
import javax.mvc.annotation.View;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cz.kramolis.tests.mvc.CdiProducer.Holder;

/**
 * MVC JAX-RS Resource.
 */
@Path("hello")
@ApplicationScoped
public class HelloController {

    @Inject
    private Models models;

    @Inject
    private Messages messages;

    @Inject @Named("hello")
    private Holder<Long> counter;

    @Inject @Current
    private Holder<Date> time;

    @GET
    @Controller
    @Produces("text/html")
    @View("hello.mustache")
    public void hello(@QueryParam("user") @DefaultValue("World") String user) {
        models.put("user", user);
        models.put("greeting", messages.getGreeting());
        models.put("startupTime", messages.getStartupTime());
        models.put("time", time.get());
        models.put("counter", counter.get());
    }

}
