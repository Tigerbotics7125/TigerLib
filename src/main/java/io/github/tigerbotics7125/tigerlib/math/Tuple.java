/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.math;

import java.util.Objects;

/**
 * This class is a simple way of dealing with a pair of objects together, rather than using a List
 * or Object[].
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 */
public class Tuple<A, B> {
    private A mA;
    private B mB;

    public Tuple(A a, B b) {
        mA = a;
        mB = b;
    }

    public static <A, B> Tuple<A, B> of(A a, B b) {
        return new Tuple<A, B>(a, b);
    }

    public A getFirst() {
        return mA;
    }

    public B getSecond() {
        return mB;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (o instanceof Tuple<?, ?>)
            try {
                Tuple<A, B> t = (Tuple<A, B>) o;
                return t.getFirst().equals(mA) && t.getSecond().equals(mB);
            } catch (ClassCastException cce) {
                return false;
            }
        else return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mA, mB);
    }
}
