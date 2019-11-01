package org.paasta.servicebroker.applifecycle.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.openpaas.paasta.bosh.director.BoshDirector;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.paasta.servicebroker.applifecycle.exception.ServiceException;
import org.paasta.servicebroker.applifecycle.model.JpaDedicatedVM;
import org.paasta.servicebroker.applifecycle.model.JpaServiceInstance;
import org.paasta.servicebroker.applifecycle.repository.JpaDedicatedVMRepository;
import org.paasta.servicebroker.applifecycle.repository.JpaServiceInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * The type App lifecycle common service.
 */
@Slf4j
@Service
public class AppLifecycleCommonService {

    @Value("${bosh.deployment_name}")
    public String deploymentName;
    @Value("${service.init_password}")
    public String initPassword;
    @Value("${service.service_admin}")
    public String serviceAdmin;

    @Autowired
    BoshDirector boshDirector;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    JpaServiceInstanceRepository jpaServiceInstanceRepository;
    @Autowired
    JpaDedicatedVMRepository jpaDedicatedVMRepository;

    /**
     * Gets service instance.
     *
     * @param serviceInstanceId the service instance id
     * @return the service instance
     */
    public ServiceInstance getServiceInstance(String serviceInstanceId) {
        JpaServiceInstance jpaServiceInstance = jpaServiceInstanceRepository.findOne(serviceInstanceId);

        if (jpaServiceInstance != null) {
            return new ServiceInstance(new CreateServiceInstanceRequest(
                    jpaServiceInstance.getServiceId(),
                    jpaServiceInstance.getPlanId(),
                    jpaServiceInstance.getOrganizationGuid(),
                    jpaServiceInstance.getSpaceGuid()
            ).withServiceInstanceId(jpaServiceInstance.getServiceInstanceId())
            ).withDashboardUrl(jpaServiceInstance.getDashboardUrl());
        }
        return null;
    }

    /**
     * Find by org guid service instance.
     *
     * @param orgGuid the org guid
     * @return the service instance
     */
    public ServiceInstance findByOrgGuid(String orgGuid) {
        JpaServiceInstance jpaServiceInstance = jpaServiceInstanceRepository.findDistinctFirstByOrganizationGuid(orgGuid);

        if (jpaServiceInstance != null) {
            return new ServiceInstance(new CreateServiceInstanceRequest(
                    jpaServiceInstance.getServiceId(),
                    jpaServiceInstance.getPlanId(),
                    jpaServiceInstance.getOrganizationGuid(),
                    jpaServiceInstance.getSpaceGuid()
            ).withServiceInstanceId(jpaServiceInstance.getServiceInstanceId()));
        }
        return null;
    }

    /**
     * Service assignment string.
     *
     * @param request the request
     * @return the string
     * @throws ServiceException the service exception
     */
    public String serviceAssignment(CreateServiceInstanceRequest request) throws ServiceException {

        String serviceInstanceId = request.getServiceInstanceId();
        String password = (String) request.getParameters().get(Constants.PARAMETERS_KEY);
        JpaDedicatedVM jpaDedicatedVM = jpaDedicatedVMRepository.findDistinctFirstByAssignmentEquals(Constants.STATUS_WATING_FOR_ASSIGNMENT);

        if (jpaDedicatedVM != null) {
            jpaDedicatedVM.setAssignment(Constants.STATUS_ASSIGNED);
            jpaDedicatedVM.setProvisionedServiceInstanceId(serviceInstanceId);
            jpaDedicatedVMRepository.save(jpaDedicatedVM);

        } else {
            throw new ServiceException("Cannot assign VM. There are no available service VM.");
        }

        // Service Admin User Passord 설정
        try {
            setPasswordServiceAdmin(jpaDedicatedVM.getIp(), password);
        } catch (Exception e) {
            jpaDedicatedVM.setAssignment(Constants.STATUS_WATING_FOR_ASSIGNMENT);
            jpaDedicatedVM.setProvisionedServiceInstanceId(null);
            jpaDedicatedVMRepository.save(jpaDedicatedVM);
            throw new ServiceException("Error in setting the Password." + e.getMessage());
        }

        return jpaDedicatedVM.getDashboardUrl();
    }

    /**
     * provisioning.
     *
     * @param serviceInstance the service instance
     */
    public void createServiceInstance(ServiceInstance serviceInstance) {
        JpaServiceInstance jpaServiceInstance = JpaServiceInstance.builder()
                .serviceInstanceId(serviceInstance.getServiceInstanceId())
                .serviceId(serviceInstance.getServiceDefinitionId())
                .planId(serviceInstance.getPlanId())
                .organizationGuid(serviceInstance.getOrganizationGuid())
                .spaceGuid(serviceInstance.getSpaceGuid())
                .dashboardUrl(serviceInstance.getDashboardUrl())
                .build();

        jpaServiceInstanceRepository.save(jpaServiceInstance);
    }


    /**
     * Proc deprovisioning.
     *
     * @param serviceInstanceId the service instance id
     * @throws ServiceException the service exception
     */
    public void procDeProvisioning(String serviceInstanceId) throws ServiceException {

        // Delete service instance data
        jpaServiceInstanceRepository.delete(serviceInstanceId);

        // Deprovision dedicated VM
        JpaDedicatedVM jpaDedicatedVM = deprovisionVM(serviceInstanceId);

        // Call Bosh RecreateVM API
        reCreateVM(jpaDedicatedVM.getVmName(), jpaDedicatedVM.getVmId());
    }

    /**
     * Deprovision vm jpa dedicated vm.
     *
     * @param serviceInstanceId the service instance id
     * @return the jpa dedicated vm
     * @throws ServiceException the service exception
     */
    JpaDedicatedVM deprovisionVM(String serviceInstanceId) throws ServiceException {
        JpaDedicatedVM jpaDedicatedVM = jpaDedicatedVMRepository.findDistinctFirstByProvisionedServiceInstanceId(serviceInstanceId);

        if (jpaDedicatedVM != null) {
            jpaDedicatedVM.setAssignment(Constants.STATUS_WATING_FOR_VM_RECREATE);
            jpaDedicatedVM.setProvisionedServiceInstanceId(null);
            jpaDedicatedVM.setProvisionedTime(null);
            jpaDedicatedVMRepository.save(jpaDedicatedVM);
            return jpaDedicatedVM;

        } else {
            throw new ServiceException("Cannot deprovision. There are no provisioned VM.");
        }
    }

    // [ Use Bosh API ]=================================================================================================
    private void reCreateVM(String vmName, String vmId) throws ServiceException {
        try {

            boolean result = boshDirector.updateInstanceState(deploymentName, vmName, vmId, Constants.JOB_STATE_RECREATE);

            if (!result) {
                log.error("##### reCreateVM :: use Bosh API ::: deploymentName :: {}, vmName :: {}, vmId :: {} ", deploymentName, vmName, vmId);
                throw new ServiceException("Failed to recreate dedecated VM :: Deployment Name [" + deploymentName + "], VM Name/VM ID [" + vmName + "/" + vmId + "]");
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException("Failed to recreate dedecated VM :: " + e.getMessage());
        }
    }

    // [ Use TAIGA API ]=================================================================================================
    private void setPasswordServiceAdmin(String url, String password) throws ServiceException {
        Gson gson = new Gson();

        String authToken = getApiAuthToken(url);

        String reqUrl = "http://" + url + Constants.TAIGA_API_CHANGE_PW;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authToken);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("current_password", initPassword);
        jsonObject.addProperty("password", password);

        String param = gson.toJson(jsonObject);

        HttpEntity<String> entity = new HttpEntity<>(param, headers);

        ResponseEntity<String> response = restTemplate.exchange(reqUrl, HttpMethod.POST, entity, String.class);

        log.info("change service admin password status :: {}", response.getStatusCode());

    }

    private String getApiAuthToken(String url) {
        Gson gson = new Gson();

        String reqUrl = "http://" + url + Constants.TAIGA_API_AUTH;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("password", initPassword);
        jsonObject.addProperty("type", "normal");
        jsonObject.addProperty("username", serviceAdmin);

        String param = gson.toJson(jsonObject);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(param, headers);

        Map response = restTemplate.exchange(reqUrl, HttpMethod.POST, entity, Map.class).getBody();
        String authToken = (String) response.get("auth_token");
        log.info("get auth token :: {}", authToken);

        return authToken;
    }

}
