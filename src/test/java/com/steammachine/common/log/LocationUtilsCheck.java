package com.steammachine.common.log;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;

/**
 * @author Vladimir Bogodukhov
 */
public class LocationUtilsCheck {

    /* -------------------------------------------------- findClassFolder -----------------------------------------  */

    @Test(expected = NullPointerException.class)
    public void testFindClassFolder_error1() {
        LocationUtils.findClassFolder(null);
    }

    @Test
    public void testFindClassFolder10() throws URISyntaxException {
        File file = new File(LocationUtils.findClassFolder(
                LocationUtilsCheck.class.getName()), "res/FakeClassToVerify.class");
        Assert.assertTrue(file.exists());
        File file2 = new File(LocationUtils.findClassFolder(
                LocationUtilsCheck.class.getName()), "res/FakeClassThatDoesNotExist.class");
        Assert.assertFalse(file2.exists());

    }

    @Test
    public void testFindClassFolder20() throws URISyntaxException {
        Assert.assertEquals(
                new File(new File(LocationUtilsCheck.class.getProtectionDomain().
                        getCodeSource().getLocation().toURI()).getPath(),
                        LocationUtilsCheck.class.getName().replace(".", "/")).getParent(),
                LocationUtils.findClassFolder(LocationUtilsCheck.class.getName())
        );
    }

    /* -------------------------------------------------- findFolderOfClass ---------------------------------------  */

    @Test(expected = NullPointerException.class)
    public void findFolderOfClass10() {
        LocationUtils.findFolderOfClass(null);
    }

    @Test
    public void findFolderOfClass20() throws URISyntaxException {
        Assert.assertEquals(
                new File(new File(LocationUtilsCheck.class.getProtectionDomain().
                        getCodeSource().getLocation().toURI()).getPath(),
                        LocationUtilsCheck.class.getName().replace(".", "/")).getParent(),

                LocationUtils.findFolderOfClass(LocationUtilsCheck.class)
        );
    }

}