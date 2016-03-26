package cz.kramolis.tests.mvc;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class HelloControllerIT {

    private static final String WEB_URL = "http://localhost:8080";

    private static CdiContainer cdiContainer;

    private static WebClient webClient;

    @BeforeClass
    public static void init() throws ServletException, NoSuchMethodException {
        cdiContainer = Main.init();

        webClient = new WebClient();
    }

    @AfterClass
    public static void destroy() {
        webClient.close();

        Main.destroy(cdiContainer);
    }

    @Test
    public void testQueryParamUser() throws Exception {
        HtmlPage page = webClient.getPage(WEB_URL + "/resources/hello?user=mvc");
        Iterator<HtmlElement> it = page.getDocumentElement().getHtmlElementsByTagName("h1").iterator();
        assertThat(it.next().getTextContent(), containsString("mvc"));
    }

    @Test
    public void testApplicationScopedGreeting() throws Exception {
        HtmlPage page = webClient.getPage(WEB_URL + "/resources/hello?user=mvc");
        Iterator<HtmlElement> it = page.getDocumentElement().getHtmlElementsByTagName("h1").iterator();
        assertThat(it.next().getTextContent(), containsString("Hello"));
    }

    @Test
    public void testApplicationScopedStartupTime() throws Exception {
        String value1 = webClient.<HtmlPage>getPage(WEB_URL + "/resources/hello?user=mvc")
                .getElementById("startup").getTextContent();
        TimeUnit.MILLISECONDS.sleep(1001);
        String value2 = webClient.<HtmlPage>getPage(WEB_URL + "/resources/hello?user=mvc")
                .getElementById("startup").getTextContent();

        assertThat(value2, is(value1));
    }

    @Test
    public void testRequestScopedTime() throws Exception {
        String value1 = webClient.<HtmlPage>getPage(WEB_URL + "/resources/hello?user=mvc")
                .getElementById("now").getTextContent();
        TimeUnit.MILLISECONDS.sleep(1001);
        String value2 = webClient.<HtmlPage>getPage(WEB_URL + "/resources/hello?user=mvc")
                .getElementById("now").getTextContent();

        assertThat(value2, not(value1));
    }

    @Test
    public void testRequestScopedCounter() throws Exception {
        Long value1 = Long.valueOf(webClient.<HtmlPage>getPage(WEB_URL + "/resources/hello?user=mvc")
                                           .getElementById("counter").getTextContent());
        Long value2 = Long.valueOf(webClient.<HtmlPage>getPage(WEB_URL + "/resources/hello?user=mvc")
                                           .getElementById("counter").getTextContent());

        assertThat(value2, is(value1 + 1));
    }

}
