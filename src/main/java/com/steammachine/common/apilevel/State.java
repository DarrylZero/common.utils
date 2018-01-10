package com.steammachine.common.apilevel;

@Api(State.MAINTAINED)
public enum State {

    /**
     * feature is for internal use - can be deleted removed or renamed - do not use in custom code
     */
    INTERNAL,

    /**
     * feature is in experimental mode - can be deleted removed or renamed - use in custom code with caution
     */
    EXPERIMENT,

    /**
     * Feature is approved to be {@link State#MAINTAINED}  but is still checked
     */
    INCUBATING,

    /**
     * Feature is checked and can be used.
     */
    MAINTAINED
}