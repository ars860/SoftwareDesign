package api;

import api.gson.Error;
import api.gson.Response;
import api.gson.ResponseWrapper;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Function;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {ApiHelper.class})
public class ApiHelperTest {
    private final ApiHelper apiHelper = new ApiHelper("123");
    private final Gson gson = new Gson();

    @Test
    public void shouldReturnOneTwoThree() throws Exception {
        InputStream stream = new ByteArrayInputStream(
                gson.toJson(new ResponseWrapper(new Response(123), null)).getBytes()
        );

        stubURL(u -> stream);

        assertThat(apiHelper.getPostsAmountInHourByTag("vse_ravno", 1), is(123));
    }

    @Test
    public void shouldFailWithErrorMessage() throws Exception {
        InputStream stream = new ByteArrayInputStream(
                gson.toJson(new ResponseWrapper(null, new Error("message"))).getBytes()
        );

        stubURL(u -> stream);

        Throwable thrown = assertThrows(ApiException.class, () -> apiHelper.getPostsAmountInHourByTag("vse_ravno", 1));
        assertThat(thrown.getMessage(), is("message"));
    }

    @Test
    public void shouldTellIfApiChanged() throws Exception {
        InputStream stream = new ByteArrayInputStream(
                "{aba : \"caba\"}".getBytes()
        );

        stubURL(u -> stream);

        Throwable thrown = assertThrows(ApiException.class, () -> apiHelper.getPostsAmountInHourByTag("vse_ravno", 1));
        assertThat(thrown.getMessage(), is("Can't parse response from VK."));
    }

    private void stubURL(Function<URL, InputStream> f) throws Exception {
        URL url = PowerMockito.mock(URL.class);

        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(url);
        PowerMockito.when(url.openStream()).thenAnswer(inv -> {
            URL obj = (URL) inv.getMock();

            return f.apply(obj);
        });

    }
}