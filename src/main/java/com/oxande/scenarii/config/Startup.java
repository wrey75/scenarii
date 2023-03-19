package com.oxande.scenarii.config;

import java.util.Arrays;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import com.oxande.scenarii.model.DBUser;

import io.quarkus.runtime.StartupEvent;

@Singleton
public class Startup {
    @Transactional
    public void loadUsers(@Observes StartupEvent evt) {
        // reset and load all test users
        // DBUser.deleteAll();
        DBUser.add("admin", "admin", Arrays.asList(DBUser.ADMIN_ROLE, DBUser.USER_ROLE));
        DBUser.add("user", "user", Arrays.asList(DBUser.USER_ROLE));
    }
}

