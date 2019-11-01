package org.paasta.servicebroker.applifecycle.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openpaas.paasta.bosh.director.BoshDirector;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.paasta.servicebroker.applifecycle.exception.ServiceException;
import org.paasta.servicebroker.applifecycle.model.JpaDedicatedVM;
import org.paasta.servicebroker.applifecycle.model.JpaRepositoryFixture;
import org.paasta.servicebroker.applifecycle.model.JpaServiceInstance;
import org.paasta.servicebroker.applifecycle.model.RequestFixture;
import org.paasta.servicebroker.applifecycle.repository.JpaDedicatedVMRepository;
import org.paasta.servicebroker.applifecycle.repository.JpaServiceInstanceRepository;
import org.paasta.servicebroker.applifecycle.service.impl.AppLifecycleCommonService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * The type App lifecycle common service test.
 */
@RunWith(SpringRunner.class)
public class AppLifecycleCommonServiceTest {

    @InjectMocks
    AppLifecycleCommonService appLifecycleCommonService;

    @Mock
    JpaServiceInstanceRepository jpaServiceInstanceRepository;
    @Mock
    JpaDedicatedVMRepository jpaDedicatedVMRepository;
    @Mock
    BoshDirector boshDirector;

    JpaServiceInstance jpaServiceInstance;
    JpaDedicatedVM jpaDedicatedVM;
    CreateServiceInstanceRequest createServiceInstanceRequest;
    ServiceInstance serviceInstance;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {

        ReflectionTestUtils.setField(appLifecycleCommonService, "deploymentName", TestConstants.DEPLOYMENT_NAME);
        ReflectionTestUtils.setField(appLifecycleCommonService, "serviceAdmin", TestConstants.SERVICE_ADMIN);
        ReflectionTestUtils.setField(appLifecycleCommonService, "initPassword", TestConstants.SERVICE_ADMIN_INIT_PASSWORD);
        createServiceInstanceRequest = RequestFixture.getCreateServiceInstanceRequest();
        Map vaildParam = new HashMap<>();
        vaildParam.put(TestConstants.PARAMETERS_KEY, TestConstants.VAILD_PARAMETER_VALUE);
        createServiceInstanceRequest.setParameters(vaildParam);

        jpaServiceInstance = JpaRepositoryFixture.getJpaServiceInstance();
        jpaDedicatedVM = JpaRepositoryFixture.getJpaDedicatedVM();
        serviceInstance = RequestFixture.getServiceInstance();

    }

    /**
     * Gets service instance test verify return.
     */
    @Test
    public void getServiceInstanceTest_VerifyReturn() {

        when(jpaServiceInstanceRepository.findOne(anyString())).thenReturn(jpaServiceInstance);

        ServiceInstance result = appLifecycleCommonService.getServiceInstance(TestConstants.SV_INSTANCE_ID);

        assertThat(result.getServiceDefinitionId(), is(jpaServiceInstance.getServiceId()));
        assertThat(result.getPlanId(), is(jpaServiceInstance.getPlanId()));
        assertThat(result.getOrganizationGuid(), is(jpaServiceInstance.getOrganizationGuid()));
        assertThat(result.getSpaceGuid(), is(jpaServiceInstance.getSpaceGuid()));
        assertThat(result.getServiceInstanceId(), is(jpaServiceInstance.getServiceInstanceId()));
        assertThat(result.getDashboardUrl(), is(jpaServiceInstance.getDashboardUrl()));
    }

    /**
     * Gets service instance test verify return is null.
     */
    @Test
    public void getServiceInstanceTest_VerifyReturnIsNull() {

        when(jpaServiceInstanceRepository.findOne(anyString())).thenReturn(null);

        ServiceInstance result = appLifecycleCommonService.getServiceInstance(TestConstants.SV_INSTANCE_ID);

        assertThat(result, is(nullValue()));
    }

    /**
     * Find by org guid test verify return.
     */
    @Test
    public void findByOrgGuidTest_VerifyReturn() {

        when(jpaServiceInstanceRepository.findDistinctFirstByOrganizationGuid(anyString())).thenReturn(jpaServiceInstance);

        ServiceInstance result = appLifecycleCommonService.findByOrgGuid(TestConstants.ORG_GUID);

        assertThat(result.getServiceDefinitionId(), is(jpaServiceInstance.getServiceId()));
        assertThat(result.getPlanId(), is(jpaServiceInstance.getPlanId()));
        assertThat(result.getOrganizationGuid(), is(jpaServiceInstance.getOrganizationGuid()));
        assertThat(result.getSpaceGuid(), is(jpaServiceInstance.getSpaceGuid()));
        assertThat(result.getServiceInstanceId(), is(jpaServiceInstance.getServiceInstanceId()));
    }

    /**
     * Find by org guid test verify return is null.
     */
    @Test
    public void findByOrgGuidTest_VerifyReturnIsNull() {

        when(jpaServiceInstanceRepository.findDistinctFirstByOrganizationGuid(anyString())).thenReturn(null);

        ServiceInstance result = appLifecycleCommonService.findByOrgGuid(TestConstants.ORG_GUID);

        assertThat(result, is(nullValue()));
    }

    /**
     * Create service instance test.
     */
    @Test
    public void createServiceInstanceTest() {

        appLifecycleCommonService.createServiceInstance(serviceInstance);

        verify(jpaServiceInstanceRepository, times(1)).save(any(JpaServiceInstance.class));
    }

    /**
     * Proc de provisioning test.
     *
     * @throws Exception the exception
     */
    @Test
    public void procDeProvisioningTest() throws Exception {

        doNothing().when(jpaServiceInstanceRepository).delete(anyString());
        when(jpaDedicatedVMRepository.findDistinctFirstByProvisionedServiceInstanceId(anyString())).thenReturn(jpaDedicatedVM);
        when(boshDirector.updateInstanceState(TestConstants.DEPLOYMENT_NAME, jpaDedicatedVM.getVmName(), jpaDedicatedVM.getVmId(), TestConstants.JOB_STATE_RECREATE)).thenReturn(true);

        appLifecycleCommonService.procDeProvisioning(TestConstants.SV_INSTANCE_ID);
    }

    /**
     * Proc de provisioning test verify recreate vm case 1.
     *
     * @throws Exception the exception
     */
    @Test
    public void procDeProvisioningTest_VerifyRecreateVM_Case1() throws Exception {

        doNothing().when(jpaServiceInstanceRepository).delete(anyString());
        when(jpaDedicatedVMRepository.findDistinctFirstByProvisionedServiceInstanceId(anyString())).thenReturn(jpaDedicatedVM);
        when(boshDirector.updateInstanceState(TestConstants.DEPLOYMENT_NAME, jpaDedicatedVM.getVmName(), jpaDedicatedVM.getVmId(), TestConstants.JOB_STATE_RECREATE)).thenReturn(false);

        assertThatThrownBy(() -> appLifecycleCommonService.procDeProvisioning(TestConstants.SV_INSTANCE_ID))
                .isInstanceOf(ServiceException.class).hasMessageContaining("Failed to recreate dedecated VM");
    }

    /**
     * Proc de provisioning test verify recreate vm case 2.
     *
     * @throws Exception the exception
     */
    @Test
    public void procDeProvisioningTest_VerifyRecreateVM_Case2() throws Exception {

        doNothing().when(jpaServiceInstanceRepository).delete(anyString());
        when(jpaDedicatedVMRepository.findDistinctFirstByProvisionedServiceInstanceId(anyString())).thenReturn(jpaDedicatedVM);
        when(boshDirector.updateInstanceState(TestConstants.DEPLOYMENT_NAME, jpaDedicatedVM.getVmName(), jpaDedicatedVM.getVmId(), TestConstants.JOB_STATE_RECREATE)).thenThrow(Exception.class);

        assertThatThrownBy(() -> appLifecycleCommonService.procDeProvisioning(TestConstants.SV_INSTANCE_ID))
                .isInstanceOf(ServiceException.class).hasMessageContaining("Failed to recreate dedecated VM");
    }

}
