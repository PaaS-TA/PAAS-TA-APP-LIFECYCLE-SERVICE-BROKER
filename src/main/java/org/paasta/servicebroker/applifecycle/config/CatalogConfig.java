package org.paasta.servicebroker.applifecycle.config;

import org.openpaas.servicebroker.model.Catalog;
import org.openpaas.servicebroker.model.DashboardClient;
import org.openpaas.servicebroker.model.Plan;
import org.openpaas.servicebroker.model.ServiceDefinition;
import org.paasta.servicebroker.applifecycle.service.impl.AppLifecycleCatalogPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * The type Catalog config.
 */
@Configuration
@EnableConfigurationProperties(AppLifecycleCatalogPropertyService.class)
public class CatalogConfig {

    private final
    AppLifecycleCatalogPropertyService appLifecycleCatalogPropertyService;


    /**
     * Instantiates a new Catalog config.
     *
     * @param appLifecycleCatalogPropertyService the app lifecycle catalog property service
     */
    @Autowired
    public CatalogConfig(AppLifecycleCatalogPropertyService appLifecycleCatalogPropertyService) {
        this.appLifecycleCatalogPropertyService = appLifecycleCatalogPropertyService;
    }

    /**
     * Catalog catalog.
     *
     * @return the catalog
     */
    @Bean
    public Catalog catalog() {
        return new Catalog(Collections.singletonList(
                new ServiceDefinition(
                        appLifecycleCatalogPropertyService.getId(),
                        appLifecycleCatalogPropertyService.getName(),
                        appLifecycleCatalogPropertyService.getDescription(),
                        appLifecycleCatalogPropertyService.isBindable(),
                        appLifecycleCatalogPropertyService.isPlanUpdatable(),
                        getPlans(),
                        appLifecycleCatalogPropertyService.getTags(),
                        appLifecycleCatalogPropertyService.getMetadata(),
                        appLifecycleCatalogPropertyService.getRequires(),
                        appLifecycleCatalogPropertyService.getDashboardClient() == null ? null :
                                new DashboardClient(appLifecycleCatalogPropertyService.getDashboardClient().getId(),
                                        appLifecycleCatalogPropertyService.getDashboardClient().getSecret(),
                                        appLifecycleCatalogPropertyService.getDashboardClient().getRedirectUri())
                ))
        );
    }

    private List<Plan> getPlans() {
        List<Plan> plans = new ArrayList<>();

        appLifecycleCatalogPropertyService.getPlans().forEach(e -> plans.add(
                new Plan(e.getId(),
                        e.getName(),
                        e.getDescription(),
                        getPlanMetadata(e.getMetadata()),
                        e.isFree())
        ));

        return plans;
    }

    private Map<String, Object> getPlanMetadata(AppLifecycleCatalogPropertyService.PlanMetaData metaData) {
        Map<String, Object> planMetadata = new HashMap<>();

        planMetadata.put("costs", getCosts(metaData.getCosts()));
        planMetadata.put("bullets", metaData.getBullets());

        return planMetadata;
    }

    private List<Map<String, Object>> getCosts(AppLifecycleCatalogPropertyService.Cost cost) {
        Map<String, Object> costsMap = new HashMap<>();

        costsMap.put("amount", cost.getAmount());
        costsMap.put("unit", cost.getUnit());

        return Collections.singletonList(costsMap);
    }

}
