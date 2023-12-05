package com.example.calendar.common.security.userDetails;

import com.example.calendar.user.entity.User;
import com.example.calendar.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class    UserDetailsServiceImpl implements UserDetailsService {
    /**
     * UserDrtailsService란 ? Spring Security에서 유저의 정보를 가져오는 인터페이스이다.
     * Spring Security에서 유저의 정보를 불러오기 위해서 구현해야하는 인터페이스로 기본 오버라이드 메서드는 아래와 같다.
     */

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { //유저의 정보를 불러와서 UserDetails로 리턴
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new UserDetailsImpl(user, user.getNickName());
    }

}
