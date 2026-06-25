package Mangement.StudentManagement.ServiceImpl;

import Mangement.StudentManagement.Exception.EnrollmentNotFoundException;
import Mangement.StudentManagement.Repository.UserRepository;
import Mangement.StudentManagement.Entity.User;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserRepository userrepo;

    public UserDetailsServiceImpl(UserRepository userrepo){
        this.userrepo=userrepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userrepo.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not Found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole().name()
                        )
                )
        );
    }

}
