package com.cwac.meetings;

import com.cwac.mongoDocs.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by David on 11/7/2015.
 * This interface exists primarly to help test without having trouble from Collections.shuffle
 */
public interface Randomizer {
    default void randomize(Collection collection) {
        Collections.shuffle((List<User>) collection);
    }
}
