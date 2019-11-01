package org.paasta.servicebroker.applifecycle.service.impl;

import org.openpaas.servicebroker.model.Catalog;
import org.openpaas.servicebroker.model.ServiceDefinition;
import org.openpaas.servicebroker.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * The type App lifecycle catalog service.
 */
@Service
public class AppLifecycleCatalogService implements CatalogService {

    private Catalog catalog;
    private Map<String, ServiceDefinition> serviceDefs = new HashMap<>();

    /**
     * Instantiates a new App lifecycle catalog service.
     *
     * @param catalog the catalog
     */
    @Autowired
    public AppLifecycleCatalogService(Catalog catalog) {
        this.catalog = catalog;
        initializeMap();
    }

    private void initializeMap() {
        for (ServiceDefinition def : catalog.getServiceDefinitions()) {
            serviceDefs.put(def.getId(), def);
        }
    }

    @Override
    public Catalog getCatalog() {
        return catalog;
    }

    @Override
    public ServiceDefinition getServiceDefinition(String serviceId) {
        return serviceDefs.get(serviceId);
    }

}
