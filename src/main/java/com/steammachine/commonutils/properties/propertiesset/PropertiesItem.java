package com.steammachine.commonutils.properties.propertiesset;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;

/**
 * Created 31.12.2017 18:10
 * @author Vladimir Bogodukhov
 */
@Api(State.EXPERIMENT)
public interface PropertiesItem<PROP_KEY, PROP_OBJ> {

    PROP_KEY key();

    PROP_OBJ object();

}
