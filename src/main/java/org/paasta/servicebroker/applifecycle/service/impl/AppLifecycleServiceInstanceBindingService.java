package org.paasta.servicebroker.applifecycle.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.openpaas.servicebroker.model.ServiceInstanceBinding;
import org.openpaas.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type App lifecycle service instance binding service.
 */
@Slf4j
@Service
public class AppLifecycleServiceInstanceBindingService implements ServiceInstanceBindingService {

    /**
     * Instantiates a new App lifecycle service instance binding service.
     */
    @Autowired
    public AppLifecycleServiceInstanceBindingService() {
    }

    @Override
    public ServiceInstanceBinding createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) throws ServiceBrokerException {
        log.debug("AppLifecycleServiceInstanceBindingService : Bind ServiceInstance :: Not Supported");
        throw new ServiceBrokerException("Not Supported");
    }

    @Override
    public ServiceInstanceBinding deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) throws ServiceBrokerException {
        log.debug("AppLifecycleServiceInstanceBindingService : Unbind ServiceInstance :: Not Supported");
        throw new ServiceBrokerException("Not Supported");
    }

}
