package com.example.ecommerce.domain.user.service;


import com.example.ecommerce.domain.cart.entity.Cart;
import com.example.ecommerce.domain.user.UserCreateForm;
import com.example.ecommerce.domain.user.UserModifyForm;
import com.example.ecommerce.domain.user.entity.SiteUser;
import com.example.ecommerce.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public SiteUser findByUsername(String username) {
        Optional<SiteUser> user = this.userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return null;
        }
        return user.get();
    }

    public void signup(UserCreateForm userCreateForm) {
        List<Cart> cartList = new ArrayList<>();
        SiteUser user = SiteUser.builder()
                .username(userCreateForm.getUsername())
                .password(passwordEncoder.encode(userCreateForm.getPassword2()))
                .nickname(userCreateForm.getNickname())
                .sex(userCreateForm.getSex())
                .age(userCreateForm.getAge())
                .email(userCreateForm.getEmail())
                .phoneNumber(userCreateForm.getPhoneNumber())
                .address(userCreateForm.getAddress())
                .role("user")
                .point(0)
                .grade("브론즈")
                .isSeller('n')
                .cartList(cartList)
                .build();

        this.userRepository.save(user);
    }

    public void addCart(SiteUser loginedUser, Cart cart) {
        List<Cart> cartList = loginedUser.getCartList();
        cartList.add(cart);
        SiteUser user = loginedUser.toBuilder()
                .cartList(cartList)
                .build();

        this.userRepository.save(user);
    }

    public SiteUser findByNickname(String nickname) {
        Optional<SiteUser> user = this.userRepository.findByNickname(nickname);
        if (user.isEmpty()) {
            return null;
        }
        return user.get();
    }

    public SiteUser findByEmail(String email) {
        Optional<SiteUser> user = this.userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return null;
        }
        return user.get();
    }

    public void modify(UserModifyForm userModifyForm, SiteUser modifyUser) {
        SiteUser user = modifyUser.toBuilder()
                .password(passwordEncoder.encode(userModifyForm.getPassword2()))
                .nickname(userModifyForm.getNickname())
                .email(userModifyForm.getEmail())
                .phoneNumber(userModifyForm.getPhoneNumber())
                .address(userModifyForm.getAddress())
                .build();

        this.userRepository.save(user);
    }
}
