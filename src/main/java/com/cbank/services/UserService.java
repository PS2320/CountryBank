package com.cbank.services;

import com.cbank.domain.security.BaseToken;
import com.cbank.domain.user.User;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * {@link com.bank.service.impl.PersistenceUserDetailService}
 */
public interface UserService extends UserDetailsService, PersistableService<User> {

    Optional<User> byUsername(String username);

    void resetPassword(String tokenId, String password);

    void passwordToken()

    void enable(String token);

    void lock(BaseToken token);

    default User save(String username, String password){
        val user = new User();
        user.setUsername(username);
        return save(setPassword(user, password));
    }

    default User setPassword(User user, String password){
        val salted = user.getSalt() + password;
        val hashCode = Hashing.sha256().hashBytes(salted.getBytes(StandardCharsets.UTF_8));
        user.setPassword(BaseEncoding.base32().lowerCase().encode(hashCode.asBytes()));
        return user;
    }
}