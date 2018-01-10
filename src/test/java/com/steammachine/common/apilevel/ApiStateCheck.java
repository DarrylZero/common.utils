package com.steammachine.common.apilevel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ApiStateCheck {

    @Test
    void testNameIntegrity() {
        Assertions.assertEquals("com.steammachine.common.apilevel.Api", Api.class.getName());
        Assertions.assertEquals("com.steammachine.common.apilevel.State", State.class.getName());
    }

}