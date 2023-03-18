package com.oxande.scenarii.model;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.IgnoreProperty;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.security.Principal;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "users")
@UserDefinition
@Setter
@Getter
public class User extends PanacheEntityBase {
    public static final String ADMIN_ROLE = "admin";
    public static final String USER_ROLE = "user";

    @Id
    @Username
    private String login;

    @Password
    @IgnoreProperty
    private String password;

    private String email;

    @Roles
    @IgnoreProperty
    public String roles;

    /**
     * Adds a new user in the database
     *
     * @param username the user name
     * @param password the unencrypted password (it will be encrypted with bcrypt)
     * @param roles the roles
     */
    public static void add(String username, String password, List<String> roles) {
        User user = new User();
        user.login = username;
        user.password = BcryptUtil.bcryptHash(password);
        user.roles = String.join(",", roles);
        user.persist();
    }

    public static User fromPrincipal(Principal principal) {
        String name = principal.getName();
        return find("login", name).singleResult();
    }

    public static User findByName(String name) {
        return find("login", name).firstResult();
    }
    
    @Override
    public boolean equals(Object other){
        if(other instanceof User){
            return StringUtils.equals(((User) other).getLogin(), this.login);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.login);
    }
}
