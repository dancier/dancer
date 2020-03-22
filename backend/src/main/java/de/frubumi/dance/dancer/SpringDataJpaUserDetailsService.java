package de.frubumi.dance.dancer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class SpringDataJpaUserDetailsService implements UserDetailsService {

    private final DancerUserRepository repository;

    @Autowired
    public SpringDataJpaUserDetailsService(DancerUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        DancerUser user = this.repository.findByName(name);
        return new User(user.getName(), user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRoles()));
    }

}
