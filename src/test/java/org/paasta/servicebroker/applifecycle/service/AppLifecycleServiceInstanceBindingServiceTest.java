package org.paasta.servicebroker.applifecycle.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.paasta.servicebroker.applifecycle.model.RequestFixture;
import org.paasta.servicebroker.applifecycle.service.impl.AppLifecycleServiceInstanceBindingService;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * The type Api gateway service instance binding service test.
 */
@RunWith(SpringRunner.class)
public class AppLifecycleServiceInstanceBindingServiceTest {

    @InjectMocks
    AppLifecycleServiceInstanceBindingService appLifecycleServiceInstanceBindingService;

    @Before
    public void setUp() throws Exception {
    }

    /**
     * Create service instance binding test.
     */
    @Test
    public void createServiceInstanceBindingTest() {
        CreateServiceInstanceBindingRequest request = RequestFixture.getCreateServiceInstanceBindingRequest();

        assertThatThrownBy(() -> appLifecycleServiceInstanceBindingService.createServiceInstanceBinding(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Not Supported");

    }

    /**
     * Delete service instance binding test.
     */
    @Test
    public void deleteServiceInstanceBindingTest() {
        DeleteServiceInstanceBindingRequest request = RequestFixture.getDeleteServiceInstanceBindingRequest();

        assertThatThrownBy(() -> appLifecycleServiceInstanceBindingService.deleteServiceInstanceBinding(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Not Supported");

    }
}
