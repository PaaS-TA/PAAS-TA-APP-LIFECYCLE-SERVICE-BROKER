package org.paasta.servicebroker.applifecycle.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openpaas.servicebroker.model.Catalog;
import org.openpaas.servicebroker.model.ServiceDefinition;
import org.paasta.servicebroker.applifecycle.model.ServiceDefinitionFixture;
import org.paasta.servicebroker.applifecycle.service.impl.AppLifecycleCatalogService;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * The type App lifecycle catalog service test.
 */
@RunWith(SpringRunner.class)
public class AppLifecycleCatalogServiceTest {

    @InjectMocks
    AppLifecycleCatalogService appLifecycleCatalogService;

    @Mock
    Catalog catalog;
    @Mock
    ServiceDefinition serviceDefinition;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {

        catalog = new Catalog(ServiceDefinitionFixture.getCatalog());
        appLifecycleCatalogService = new AppLifecycleCatalogService(catalog);
    }

    /**
     * Gets catalog test.
     */
    @Test
    public void getCatalogTest() {
        Catalog result = appLifecycleCatalogService.getCatalog();

        assertThat(result.getServiceDefinitions().get(0).getId(), is(ServiceDefinitionFixture.getService().getId()));
        assertThat(result.getServiceDefinitions().get(0).getName(), is(ServiceDefinitionFixture.getService().getName()));
        assertThat(result.getServiceDefinitions().get(0).isBindable(), is(ServiceDefinitionFixture.getService().isBindable()));
        assertThat(result.getServiceDefinitions().get(0).isPlanUpdatable(), is(ServiceDefinitionFixture.getService().isPlanUpdatable()));
    }

    /**
     * Gets service definition valid return.
     */
    @Test
    public void getServiceDefinitionTest() {
        serviceDefinition = appLifecycleCatalogService.getServiceDefinition(TestConstants.SERVICES_ID);

        assertThat(serviceDefinition.getId(), is(ServiceDefinitionFixture.getService().getId()));
        assertThat(serviceDefinition.getName(), is(ServiceDefinitionFixture.getService().getName()));
        assertThat(serviceDefinition.isBindable(), is(ServiceDefinitionFixture.getService().isBindable()));
        assertThat(serviceDefinition.isPlanUpdatable(), is(ServiceDefinitionFixture.getService().isPlanUpdatable()));
    }
}
