package cz.kramolis.tests.mvc;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainer;
import io.undertow.servlet.api.ServletInfo;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.jboss.weld.servlet.WeldInitialListener;
import org.jboss.weld.servlet.WeldTerminalListener;

/**
 * Main App.
 */
public class Main {

    static CdiContainer init() throws ServletException, NoSuchMethodException {
        CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();

        cdiContainer.boot();
        cdiContainer.getContextControl().startContext(ApplicationScoped.class);

        initServlet();

        return cdiContainer;
    }

    static void destroy(CdiContainer cdiContainer) {
        cdiContainer.getContextControl().stopContext(ApplicationScoped.class);
        cdiContainer.shutdown();
    }

    public static void main(String[] args) throws InterruptedException, ServletException, NoSuchMethodException {
        CdiContainer cdiContainer = null;
        try {
            cdiContainer = init();

            System.out.println("Stop the application using CTRL+C");
            Thread.currentThread().join();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            destroy(cdiContainer);
        }
    }

    private static void initServlet() throws ServletException, NoSuchMethodException {
        DeploymentInfo deploymentInfo = new DeploymentInfo()
                .addListener(Servlets.listener(WeldInitialListener.class))
                .addListener(Servlets.listener(WeldTerminalListener.class))
                .setContextPath("/")
                .setDeploymentName("standalone-javax-mvc-app")
                .addServlets(
                        createServletInfo("/resources/*", "JAX-RS Resources", org.glassfish.jersey.servlet.ServletContainer.class)
                )
                .setClassIntrospecter(CdiClassIntrospecter.INSTANCE)
                .setAllowNonStandardWrappers(true)
                .setClassLoader(ClassLoader.getSystemClassLoader());

        ServletContainer servletContainer = Servlets.defaultContainer();
        DeploymentManager deploymentManager = servletContainer.addDeployment(deploymentInfo);
        deploymentManager.deploy();

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "0.0.0.0")
                .setHandler(deploymentManager.start())
                .build();
        server.start();
    }

    private static ServletInfo createServletInfo(String mapping, String servletName, Class<? extends Servlet> servlet)
            throws NoSuchMethodException {
        ServletInfo servletInfo = Servlets
                .servlet(servletName, servlet)
                .setAsyncSupported(true)
                .setLoadOnStartup(1)
                .addMapping(mapping);
        servletInfo.setInstanceFactory(CdiClassIntrospecter.INSTANCE.createInstanceFactory(servlet));

        return servletInfo;
    }

}
