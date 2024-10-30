package org.example.testsecurity.service;

import org.example.testsecurity.dto.JoinDto;
import org.example.testsecurity.entity.UserEntity;
import org.example.testsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDto joinDto) {
        System.out.println("JoinService.joinProcess");

        // DB에 동일한 username이 존재하는지 확인한다.
        boolean isExists = userRepository.existsByUsername(joinDto.getUsername());
        if (isExists) {
            System.out.println("User already exists");
            return;
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(joinDto.getUsername());
        // 사용자가 입력한 비밀번호를 암호화한다.
        userEntity.setPassword(bCryptPasswordEncoder.encode(joinDto.getPassword()));

        // 사용자 권한 부여
//        userEntity.setRole("ROLE_USER");

        // 관리자 권한 부여
        userEntity.setRole("ROLE_ADMIN");

        userRepository.save(userEntity);
    }
}
