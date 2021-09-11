package com.utilities.aem.v1;

import com.utilities.aem.v1.impl.PageUtilityImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class PageUtilityTest {

    public final AemContext aemContext = new AemContext();
    private final PageUtility subject = new PageUtilityImpl();

    private final ResourceResolver resourceResolver = aemContext.resourceResolver();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllComponentsCountInPage() {
        Map<String, Integer> expected = new HashMap<>();
        //test for empty path
        Assertions.assertEquals(subject.getAllComponentsCountInPage(resourceResolver, null),
                expected);
        Assertions.assertEquals(subject.getAllComponentsCountInPage(resourceResolver, null),
                expected);

        //test for non-empty path
        Assertions.assertEquals(subject.getAllComponentsCountInPage(resourceResolver, "/content/we-retail"),
                expected);
    }

    @Test
    void verifyComponentExistsInPage() {
    }

    @Test
    void getAllInstancesOfComponent() {
    }
}