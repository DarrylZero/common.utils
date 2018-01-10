package com.steammachine.common.list;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author Vladimir Bogodukhov
 */
public class ListBuilderCheck {

    @Test
    public void fullName() {
        Assert.assertEquals("com.steammachine.common.list.ListBuilder", ListBuilder.class.getName());
    }

    /* ------------------------------------------------------ add ------------------------------------------------- */

    @Test
    public void add10() {
        Assert.assertEquals(Arrays.asList("A", "B", "C"), ListBuilder.of().add("A", "B", "C").unmodifiedList());
    }

    @Test
    public void add20() {
        Assert.assertEquals(Arrays.asList("A", "B", "C"), ListBuilder.of().add("A").add("B").add("C").unmodifiedList());
    }

    /* ------------------------------------------------------ conf ---------------------------------------------- */

    @Test
    public void conf10() {
        Assert.assertEquals(Arrays.asList("A", "B", "C"),
                ListBuilder.of().conf(builder -> builder.add("A").add("B").add("C")).unmodifiedList());
    }

    @Test
    public void conf20() {
        Assert.assertEquals(Collections.emptyList(),
                ListBuilder.of().conf(builder -> builder.add("A").add("B").add("C")).
                        conf(ListBuilder::clear).unmodifiedList());
    }

    @Test
    public void conf30() {
        Assert.assertEquals(Arrays.asList("A", "B", "C"),
                ListBuilder.of().conf(builder -> builder.addList(Arrays.asList("A", "B", "C"))).
                        unmodifiedList());
    }

    /* ------------------------------------------------------ addList ---------------------------------------------- */

    @Test
    public void addList10() {
        Assert.assertEquals(Arrays.asList("A", "B", "C"),
                ListBuilder.of().addList(Arrays.asList("A", "B", "C")).unmodifiedList());
    }


}