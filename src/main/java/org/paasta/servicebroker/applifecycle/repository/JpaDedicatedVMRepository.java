package org.paasta.servicebroker.applifecycle.repository;

import org.paasta.servicebroker.applifecycle.model.JpaDedicatedVM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Jpa Dedicated VM repository.
 */
@Repository
public interface JpaDedicatedVMRepository extends JpaRepository<JpaDedicatedVM, String> {

    JpaDedicatedVM findDistinctFirstByAssignmentEquals(int assignment);

    JpaDedicatedVM findDistinctFirstByProvisionedServiceInstanceId(String serviceInstanceId);
}
