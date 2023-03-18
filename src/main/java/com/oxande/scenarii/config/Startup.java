package com.oxande.scenarii.config;

import java.util.Arrays;
import java.util.Collections;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import com.oxande.scenarii.model.User;

import io.quarkus.runtime.StartupEvent;

@Singleton
public class Startup {
    @Transactional
    public void loadUsers(@Observes StartupEvent evt) {
        // reset and load all test users
        User.deleteAll();
        User.add("admin", "admin", Arrays.asList(User.ADMIN_ROLE, User.USER_ROLE));
        User.add("user", "user", Arrays.asList(User.USER_ROLE));
    }
}

