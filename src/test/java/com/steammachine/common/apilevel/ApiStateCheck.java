package com.steammachine.common.apilevel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiStateCheck {

    @Test
    void testNameIntegrity() {
        assertEquals("com.steammachine.common.apilevel.Api", Api.class.getName());
        assertEquals("com.steammachine.common.apilevel.State", State.class.getName());
    }

}