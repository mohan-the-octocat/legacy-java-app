package com.delivery.core.domain;

import lombok.Value;

@Value
public class PGLocation {
    private final Identity id;
    private final String name;
    private final String address;
    private final String managerContact;

    public static PGLocation newInstance(String name, String address, String managerContact) {
        return new PGLocation(Identity.newIdentity(), name, address, managerContact);
    }
}
