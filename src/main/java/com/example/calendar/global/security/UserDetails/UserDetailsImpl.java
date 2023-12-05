package com.example.calendar.global.security.UserDetails;

import com.example.calendar.entity.User;
import com.example.calendar.entity.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    /**
     * UserDrtails란 ? Spring Security에서 사용자의 정보를 담는 인터페이스
     */

    private final User user;
    private final String username;

    public UserDetailsImpl(User user, String username) {
        this.user = user;
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {  //계정의 권한 목록을 리턴
        UserRoleEnum role = user.getRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public String getUsername() {  //계정의 고유한 값을 리턴 ( ex : DB PK값, 중복이 없는 이메일 값 )
        return this.username;
    }

    @Override
    public String getPassword() { //계정의 비밀번호를 리턴
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {  //계정의 만료 여부 리턴  기본값 : true ( 만료 안됨 )
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {   //계정의 잠김 여부 리턴  기본값 : true true ( 잠기지 않음 )
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {  // 비밀번호 만료 여부 리턴    기본값 : true ( 만료 안됨 )
        return false;
    }

    @Override
    public boolean isEnabled() {    //계정의 활성화 여부 리턴 기본값 : true ( 활성화 됨 )
        return false;
    }
}
