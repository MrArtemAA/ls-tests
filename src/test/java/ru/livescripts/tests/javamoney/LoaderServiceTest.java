package ru.livescripts.tests.javamoney;

import org.javamoney.moneta.spi.LoaderService;
import org.junit.Test;

import javax.money.spi.Bootstrap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoaderServiceTest {
    private static final String RESOURCE_ID = "CBRFCurrentRateProvider";
    private static final LoaderService LOADER = Bootstrap.getService(LoaderService.class);

    @Test
    public void testAddResourceToLoaderService() {
        assertTrue(LOADER.getResourceIds().contains(RESOURCE_ID));
    }

    @Test
    public void testLoadDataFromCustomResource() throws IOException {
        LOADER.addLoaderListener((resourceId, is) -> {
            assertEquals(RESOURCE_ID, resourceId);
            try {
                new BufferedReader(new InputStreamReader(is, "windows-1251")).lines()
                        .forEach(System.out::println);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }, RESOURCE_ID);
        LOADER.loadData(RESOURCE_ID);
    }

}
