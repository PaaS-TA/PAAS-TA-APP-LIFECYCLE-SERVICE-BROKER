package org.paasta.servicebroker.applifecycle.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.exception.ServiceInstanceExistsException;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.openpaas.servicebroker.model.UpdateServiceInstanceRequest;
import org.paasta.servicebroker.applifecycle.exception.ServiceException;
import org.paasta.servicebroker.applifecycle.model.RequestFixture;
import org.paasta.servicebroker.applifecycle.service.impl.AppLifecycleCommonService;
import org.paasta.servicebroker.applifecycle.service.impl.AppLifecycleServiceInstanceService;
import org.paasta.servicebroker.applifecycle.service.impl.Constants;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * The type App lifecycle service instance service test.
 */
@RunWith(SpringRunner.class)
public class AppLifecycleServiceInstanceServiceTest {

    @InjectMocks
    AppLifecycleServiceInstanceService appLifecycleServiceInstanceService;

    @Mock
    AppLifecycleCommonService appLifecycleCommonService;

    ServiceInstance serviceInstance;
    CreateServiceInstanceRequest createServiceInstanceRequest;
    DeleteServiceInstanceRequest deleteServiceInstanceRequest;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        Constants constants = new Constants();
        createServiceInstanceRequest = RequestFixture.getCreateServiceInstanceRequest();
        deleteServiceInstanceRequest = RequestFixture.getDeleteServiceInstanceRequest();
        serviceInstance = RequestFixture.getServiceInstance();
    }

    //----------------[ createServiceInstance Test]

    /**
     * Create service instance test verify param.
     */
    @Test
    public void createServiceInstanceTest_VerifyParam() {
        // createServiceInstanceRequest parameter 유효성 체크
        Map reqParam = new HashMap<>();

        // case 1.  parameter 가 NULL 인 경우
        createServiceInstanceRequest.setParameters(null);
        assertThatThrownBy(() -> appLifecycleServiceInstanceService.createServiceInstance(createServiceInstanceRequest))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Required");

        // case 2. parameter 가 Empty 인 경우
        createServiceInstanceRequest.setParameters(reqParam);
        assertThatThrownBy(() -> appLifecycleServiceInstanceService.createServiceInstance(createServiceInstanceRequest))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Required");

        // case 3. parameter 에 필수 파라미터 키가 포함되어 있지 않은 경우
        reqParam.put("pw", "Test123");
        createServiceInstanceRequest.setParameters(reqParam);
        assertThatThrownBy(() -> appLifecycleServiceInstanceService.createServiceInstance(createServiceInstanceRequest))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Required");

        // case 4. Parameter 값의 유효성 체크
        reqParam = new HashMap<>();
        reqParam.put(TestConstants.PARAMETERS_KEY, "test");
        createServiceInstanceRequest.setParameters(reqParam);
        assertThatThrownBy(() -> appLifecycleServiceInstanceService.createServiceInstance(createServiceInstanceRequest))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("password does not meet the requirements.");
    }

    /**
     * Create service instance test verify service instance.
     */
    @Test
    public void createServiceInstanceTest_VerifyServiceInstance() {

        // 서비스 인스턴스 Guid Check
        Map vaildParam = new HashMap<>();
        vaildParam.put(TestConstants.PARAMETERS_KEY, TestConstants.VAILD_PARAMETER_VALUE);
        createServiceInstanceRequest.setParameters(vaildParam);

        when(appLifecycleCommonService.getServiceInstance(anyString())).thenReturn(serviceInstance);

        assertThatThrownBy(() -> appLifecycleServiceInstanceService.createServiceInstance(createServiceInstanceRequest))
                .isInstanceOf(ServiceInstanceExistsException.class);

    }

    /**
     * Create service instance test verify org.
     */
    @Test
    public void createServiceInstanceTest_VerifyOrg() {

        // Org Guid Check (Org 당 1개)
        Map vaildParam = new HashMap<>();
        vaildParam.put(TestConstants.PARAMETERS_KEY, TestConstants.VAILD_PARAMETER_VALUE);
        createServiceInstanceRequest.setParameters(vaildParam);

        when(appLifecycleCommonService.getServiceInstance(anyString())).thenReturn(null);
        when(appLifecycleCommonService.findByOrgGuid(anyString())).thenReturn(serviceInstance);

        assertThatThrownBy(() -> appLifecycleServiceInstanceService.createServiceInstance(createServiceInstanceRequest))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("already exists in your organization");

    }

    /**
     * Create service instance test verify return.
     *
     * @throws ServiceBrokerException         the service broker exception
     * @throws ServiceInstanceExistsException the service instance exists exception
     */
    @Test
    public void createServiceInstanceTest_VerifyReturn() throws ServiceBrokerException, ServiceInstanceExistsException {

        Map vaildParam = new HashMap<>();
        vaildParam.put(TestConstants.PARAMETERS_KEY, TestConstants.VAILD_PARAMETER_VALUE);
        createServiceInstanceRequest.setParameters(vaildParam);

        when(appLifecycleCommonService.getServiceInstance(anyString())).thenReturn(null);
        when(appLifecycleCommonService.findByOrgGuid(anyString())).thenReturn(null);
        when(appLifecycleCommonService.serviceAssignment(createServiceInstanceRequest)).thenReturn(TestConstants.DASHBOARD_URL);
        doNothing().when(appLifecycleCommonService).createServiceInstance(serviceInstance);

        ServiceInstance result = appLifecycleServiceInstanceService.createServiceInstance(createServiceInstanceRequest);

        assertThat(result.getServiceInstanceId(), is(serviceInstance.getServiceInstanceId()));
        assertThat(result.getServiceDefinitionId(), is(serviceInstance.getServiceDefinitionId()));
        assertThat(result.getOrganizationGuid(), is(serviceInstance.getOrganizationGuid()));
        assertThat(result.getPlanId(), is(serviceInstance.getPlanId()));
        assertThat(result.getSpaceGuid(), is(serviceInstance.getSpaceGuid()));
        assertThat(result.getDashboardUrl(), is(TestConstants.DASHBOARD_URL));
    }

    //----------------[ getServiceInstance Test]

    /**
     * Gets service instance test.
     */
    @Test
    public void getServiceInstanceTest() {

        when(appLifecycleCommonService.getServiceInstance(anyString())).thenReturn(serviceInstance);

        ServiceInstance result = appLifecycleServiceInstanceService.getServiceInstance(TestConstants.SV_INSTANCE_ID);

        assertThat(result.getServiceInstanceId(), is(serviceInstance.getServiceInstanceId()));
        assertThat(result.getServiceDefinitionId(), is(serviceInstance.getServiceDefinitionId()));
        assertThat(result.getOrganizationGuid(), is(serviceInstance.getOrganizationGuid()));
        assertThat(result.getPlanId(), is(serviceInstance.getPlanId()));
        assertThat(result.getSpaceGuid(), is(serviceInstance.getSpaceGuid()));
    }

    //----------------[ deleteServiceInstance Test]

    /**
     * Delete service instance test verify service instance.
     *
     * @throws ServiceException the service exception
     */
    @Test
    public void deleteServiceInstanceTest_VerifyServiceInstance() throws ServiceException {
        deleteServiceInstanceRequest = RequestFixture.getDeleteServiceInstanceRequest();

        when(appLifecycleCommonService.getServiceInstance(anyString())).thenReturn(null);

        ServiceInstance result = appLifecycleServiceInstanceService.deleteServiceInstance(deleteServiceInstanceRequest);

        verify(appLifecycleCommonService, times(0)).procDeProvisioning(deleteServiceInstanceRequest.getServiceInstanceId());
        assertThat(result, is(nullValue()));

    }

    /**
     * Delete service instance test deprovisioning.
     *
     * @throws ServiceException the service exception
     */
    @Test
    public void deleteServiceInstanceTest_Deprovisioning() throws ServiceException {
        deleteServiceInstanceRequest = RequestFixture.getDeleteServiceInstanceRequest();

        when(appLifecycleCommonService.getServiceInstance(anyString())).thenReturn(serviceInstance);

        doNothing().when(appLifecycleCommonService).procDeProvisioning(anyString());

        ServiceInstance result = appLifecycleServiceInstanceService.deleteServiceInstance(deleteServiceInstanceRequest);

        verify(appLifecycleCommonService).procDeProvisioning(deleteServiceInstanceRequest.getServiceInstanceId());

        assertThat(result, is(serviceInstance));

    }

    /**
     * Update service instance test.
     */
    @Test
    public void updateServiceInstanceTest() {
        UpdateServiceInstanceRequest request = RequestFixture.getUpdateServiceInstanceRequest();

        assertThatThrownBy(() -> appLifecycleServiceInstanceService.updateServiceInstance(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Not Supported");
    }
}
