package org.paasta.servicebroker.applifecycle.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.paasta.servicebroker.applifecycle.model.ServiceDefinitionFixture;
import org.paasta.servicebroker.applifecycle.service.impl.AppLifecycleCatalogPropertyService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * The type App lifecycle catalog property service test.
 */
@RunWith(SpringRunner.class)
public class AppLifecycleCatalogPropertyServiceTest {

    @InjectMocks
    AppLifecycleCatalogPropertyService appLifecycleCatalogPropertyService;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {

    }

    /**
     * Gets method valid return.
     *
     * @throws Exception the exception
     */
    @Test
    public void catalogPropertyTest() throws Exception {

        ReflectionTestUtils.setField (appLifecycleCatalogPropertyService, "id", TestConstants.SERVICES_ID);
        ReflectionTestUtils.setField (appLifecycleCatalogPropertyService, "name", TestConstants.SERVICES_NAME);
        ReflectionTestUtils.setField (appLifecycleCatalogPropertyService, "description", TestConstants.SERVICES_DESCRIPTION);
        ReflectionTestUtils.setField (appLifecycleCatalogPropertyService, "bindable", TestConstants.SERVICES_BINDABLE);
        ReflectionTestUtils.setField (appLifecycleCatalogPropertyService, "tags", TestConstants.SERVICES_TAGS);
        ReflectionTestUtils.setField (appLifecycleCatalogPropertyService, "metadata", ServiceDefinitionFixture.getMetadata());
        ReflectionTestUtils.setField (appLifecycleCatalogPropertyService, "requires", TestConstants.SERVICES_REQUIRES);
        ReflectionTestUtils.setField (appLifecycleCatalogPropertyService, "planUpdatable", TestConstants.SERVICES_PLAN_UPDATABLE);
        ReflectionTestUtils.setField (appLifecycleCatalogPropertyService, "plans", ServiceDefinitionFixture.getPlans());
        ReflectionTestUtils.setField (appLifecycleCatalogPropertyService, "dashboardClient", ServiceDefinitionFixture.getDashboardClient());

        // getId()
        assertThat(appLifecycleCatalogPropertyService.getId(),is(ServiceDefinitionFixture.getService().getId()));
        // getName()
        assertThat(appLifecycleCatalogPropertyService.getName(),is(ServiceDefinitionFixture.getService().getName()));
        // getDescription()
        assertThat(appLifecycleCatalogPropertyService.getDescription(),is(ServiceDefinitionFixture.getService().getDescription()));
        // isBindable
        assertThat(appLifecycleCatalogPropertyService.isBindable(),is(ServiceDefinitionFixture.getService().isBindable()));
        // getTags()
        assertThat(appLifecycleCatalogPropertyService.getTags(),is(ServiceDefinitionFixture.getService().getTags()));
        // getMetadata()
        assertThat(appLifecycleCatalogPropertyService.getMetadata(),is(ServiceDefinitionFixture.getMetadata()));
        // getRequires()
        assertThat(appLifecycleCatalogPropertyService.getRequires(),is(ServiceDefinitionFixture.getService().getRequires()));
        // isPlanUpdatable()
        assertThat(appLifecycleCatalogPropertyService.isPlanUpdatable(),is(ServiceDefinitionFixture.getService().isPlanUpdatable()));
        // getPlans()
        assertThat(appLifecycleCatalogPropertyService.getPlans().get(0).getId(),is(ServiceDefinitionFixture.getPlans().get(0).getId()));
        assertThat(appLifecycleCatalogPropertyService.getPlans().get(0).getId(),is(ServiceDefinitionFixture.getPlans().get(0).getId()));
        assertThat(appLifecycleCatalogPropertyService.getPlans().get(0).getName(),is(ServiceDefinitionFixture.getPlans().get(0).getName()));
        assertThat(appLifecycleCatalogPropertyService.getPlans().get(0).getDescription(),is(ServiceDefinitionFixture.getPlans().get(0).getDescription()));
        assertThat(appLifecycleCatalogPropertyService.getPlans().get(0).getMetadata().getBullets(),is(ServiceDefinitionFixture.getPlans().get(0).getMetadata().getBullets()));
        assertThat(appLifecycleCatalogPropertyService.getPlans().get(0).getMetadata().getCosts().getAmount(),is(ServiceDefinitionFixture.getPlans().get(0).getMetadata().getCosts().getAmount()));
        assertThat(appLifecycleCatalogPropertyService.getPlans().get(0).getMetadata().getCosts().getUnit(),is(ServiceDefinitionFixture.getPlans().get(0).getMetadata().getCosts().getUnit()));
        assertThat(appLifecycleCatalogPropertyService.getPlans().get(0).isFree(),is(ServiceDefinitionFixture.getPlans().get(0).isFree()));
        // getDashboardClient()
        assertThat(appLifecycleCatalogPropertyService.getDashboardClient().getId(),is(ServiceDefinitionFixture.getDashboardClient().getId()));
        assertThat(appLifecycleCatalogPropertyService.getDashboardClient().getRedirectUri(),is(ServiceDefinitionFixture.getDashboardClient().getRedirectUri()));
        assertThat(appLifecycleCatalogPropertyService.getDashboardClient().getSecret(),is(ServiceDefinitionFixture.getDashboardClient().getSecret()));
    }

}
