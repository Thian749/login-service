package com.team4.demoauth.service;

import com.team4.demoauth.entity.UserInfo;
import com.team4.demoauth.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class   UserInfoService implements UserDetailsService {
    @Autowired
    private UserInfoRepository repository; // repositorio de acceso de la base de datos

    @Autowired
    private PasswordEncoder encoder;// contraseña

    //este metodo se usa para cargar al usuario con su respectivo nombre
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByName(username);
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
    }
    //este metodo es para adicionar un nuevo usuario

    public String addUser(UserInfo userInfo){
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User Added Successfully";
    }
}
