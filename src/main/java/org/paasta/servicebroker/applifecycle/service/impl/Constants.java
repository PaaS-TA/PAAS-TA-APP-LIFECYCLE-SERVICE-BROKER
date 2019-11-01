package org.paasta.servicebroker.applifecycle.service.impl;

/**
 * The type Constants.
 */
public class Constants {

    /** The constant STATUS_WATING_FOR_ASSIGNMENT. */
    public static final int STATUS_WATING_FOR_ASSIGNMENT = 0;
    /** The constant STATUS_WATING_FOR_VM_RECREATE. */
    public static final int STATUS_WATING_FOR_VM_RECREATE = 1;
    /** The constant STATUS_ASSIGNED. */
    public static final int STATUS_ASSIGNED = 2;
    /** The constant JOB_STATE_RECREATE. */
    public static final String JOB_STATE_RECREATE = "recreate";

    /** The constant PARAMETERS_KEY_PASSWORD. */
    public static final String PARAMETERS_KEY_PASSWORD = "password";
    /** The constant TAIGA_API_AUTH. */
    public static final String TAIGA_API_AUTH = "/api/v1/auth";
    /** The constant TAIGA_API_CHANGE_PW. */
    public static final String TAIGA_API_CHANGE_PW = "/api/v1/users/change_password";
}