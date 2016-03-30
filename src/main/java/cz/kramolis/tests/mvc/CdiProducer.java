package cz.kramolis.tests.mvc;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * CDI Producer of
 */
@ApplicationScoped
public class CdiProducer {

    private long helloCounter;

    @Produces
    @ApplicationScoped
    public ServletContainer createServletContainer() {
        final ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(HelloController.class);
        resourceConfig.property("javax.mvc.engine.ViewEngine.viewFolder", "META-INF/views/");

        return new ServletContainer(resourceConfig);
    }

    @Produces
    @Named("hello")
    @RequestScoped
    public Holder<Long> getHelloCounter() {
        return new Holder<>(++helloCounter);
    }

    @Produces
    @Current
    @RequestScoped
    public Holder<Date> getCurrentDate() {
        return new Holder<>(new Date());
    }

    //
    // class Holder
    //

    /**
     * CDI instance holder for non proxyable classes.
     *
     * @param <T> type of holding object
     */
    public static class Holder<T> {
        private T instance;

        public Holder() {
        }

        public Holder(T instance) {
            this.instance = instance;
        }

        public T get() {
            return instance;
        }

    } // class Holder

}
