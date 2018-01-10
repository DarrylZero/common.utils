package com.steammachine.methodfinder; // #LFE если меняется только пакет - это допустимо.


/**
 * @author Vladimir Bogodukhov
 *         Не менять
 *         расположение методов !!! Этого файл класса (исходник) и получаемый при этом класс используется для проверки
 *         расположения (позиции) методов
 *         методов
 *         .
 *         .
 *         .
 **/
public abstract class ClazzToFindMethods {

    public static void methodToFind() {
        nop();
    }

    public static void methodToFind(int i) {
        nop();
    }

    public static void methodToFind(int i, int i2) {
        nop();
        nop();
    }

    public void methodToFind(int i, int i2, int i3) {
        nop();
        nop();
        nop();
        nop();
        nop();
        nop();
        nop();
        nop();
        nop();
        nop();
        nop();
        nop();
        nop();
        nop();
        nop();
        nop();
        nop();
        nop();
        nop();
        nop();
        nop();
        nop();
        nop();
        nop();
        /*
         *  Three lines are skipped
         */
        nop();
    }

    abstract public void methodToFind(int i, int i2, int i3, int i4);

    private static void nop() {
    }

}
