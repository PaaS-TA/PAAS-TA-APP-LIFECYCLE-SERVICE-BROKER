package org.paasta.servicebroker.applifecycle.model;

import org.openpaas.servicebroker.model.Plan;
import org.openpaas.servicebroker.model.ServiceDefinition;
import org.paasta.servicebroker.applifecycle.service.TestConstants;
import org.paasta.servicebroker.applifecycle.service.impl.AppLifecycleCatalogPropertyService;

import java.util.*;

/**
 * The type Service definition fixture.
 */
public class ServiceDefinitionFixture {

    /**
     * Gets service.
     *
     * @return the service
     */
    public static ServiceDefinition getService() {

        return new ServiceDefinition(
                TestConstants.SERVICES_ID,
                TestConstants.SERVICES_NAME,
                TestConstants.SERVICES_DESCRIPTION,
                TestConstants.SERVICES_BINDABLE,
                TestConstants.SERVICES_PLAN_UPDATABLE,
                Arrays.asList(
                        new Plan(TestConstants.SERVICES_PLANS_ID,
                                TestConstants.SERVICES_PLANS_NAME,
                                TestConstants.SERVICES_PLANS_DESCRIPTION,
                                null)),
                TestConstants.SERVICES_TAGS,
                getMetadata(),
                TestConstants.SERVICES_REQUIRES,
                null);
    }

    /**
     * Gets metadata.
     *
     * @return the metadata
     */
    public static Map<String, Object> getMetadata() {
        // Service Metadata
        Map<String, Object> metadata = new HashMap<String, Object>();
        metadata.put("displayName", TestConstants.SERVICES_METADATA_DISPLAYNAME);
        metadata.put("imageUrl", TestConstants.SERVICES_METADATA_IMAGEURL);
        metadata.put("longDescription", TestConstants.SERVICES_METADATA_LONGDESCRIPTION);
        metadata.put("providerDisplayName", TestConstants.SERVICES_METADATA_PROVIDERDISPLAYNAME);
        metadata.put("documentationUrl", TestConstants.SERVICES_METADATA_DOCUMENTATIONURL);
        metadata.put("supportUrl", TestConstants.SERVICES_METADATA_SUPPORTURL);
        return metadata;
    }

    /**
     * Gets catalog.
     *
     * @return the catalog
     */
    public static List<ServiceDefinition> getCatalog() {
        List<ServiceDefinition> result = new ArrayList<>();
        result.add(getService());

        return result;
    }

    /**
     * Gets plans.
     *
     * @return the plans
     */
    public static List<AppLifecycleCatalogPropertyService.Plan> getPlans() {
        AppLifecycleCatalogPropertyService.Plan plan = new AppLifecycleCatalogPropertyService.Plan();

        plan.setId(TestConstants.SERVICES_PLANS_ID);
        plan.setName(TestConstants.SERVICES_PLANS_NAME);
        plan.setDescription(TestConstants.SERVICES_PLANS_DESCRIPTION);
        plan.setMetadata(getPlanMetaData());
        plan.setFree(TestConstants.SERVICES_PLANS_FREE);

        List<AppLifecycleCatalogPropertyService.Plan> plans = Arrays.asList(plan);

        return plans;
    }

    /**
     * Gets plan meta data.
     *
     * @return the plan meta data
     */
    public static AppLifecycleCatalogPropertyService.PlanMetaData getPlanMetaData() {
        List<String> bluets = Arrays.asList("UNIT TEST", "CATALOG");

        AppLifecycleCatalogPropertyService.PlanMetaData planMetaData = new AppLifecycleCatalogPropertyService.PlanMetaData();

        planMetaData.setBullets(bluets);
        planMetaData.setCosts(getCost());

        return planMetaData;
    }


    /**
     * Gets cost.
     *
     * @return the cost
     */
    public static AppLifecycleCatalogPropertyService.Cost getCost() {
        AppLifecycleCatalogPropertyService.Cost cost = new AppLifecycleCatalogPropertyService.Cost();

        Map<String, Object> amount = new HashMap<>();
        amount.put("usd", 99.0);
        cost.setAmount(amount);
        cost.setUnit("MONTHLY");
        return cost;
    }


    /**
     * Gets dashboard client.
     *
     * @return the dashboard client
     */
    public static AppLifecycleCatalogPropertyService.DashboardClient getDashboardClient() {
        AppLifecycleCatalogPropertyService.DashboardClient dashboardClient = new AppLifecycleCatalogPropertyService.DashboardClient();

        dashboardClient.setId(TestConstants.DASHBOARDCLIENT_ID);
        dashboardClient.setSecret(TestConstants.DASHBOARDCLIENT_SECRET);
        dashboardClient.setRedirectUri(TestConstants.DASHBOARDCLIENT_REDIRECTURI);

        return dashboardClient;

    }
}
