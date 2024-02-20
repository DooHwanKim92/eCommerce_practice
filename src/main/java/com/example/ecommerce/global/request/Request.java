package com.example.ecommerce.global.request;


import com.example.ecommerce.domain.category.entity.Category;
import com.example.ecommerce.domain.category.service.CategoryService;
import com.example.ecommerce.domain.user.entity.SiteUser;
import com.example.ecommerce.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.List;

@Component
@RequestScope
public class Request {
    private final UserService userService;
    private final CategoryService categoryService;
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final HttpSession session;
    private User user;
    @Setter
    private SiteUser siteUser = null;

    public Request(UserService userService, CategoryService categoryService, HttpServletRequest req, HttpServletResponse resp, HttpSession session) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.req = req;
        this.resp = resp;
        this.session = session;

        // 현재 로그인한 회원의 인증정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User) {
            this.user = (User) authentication.getPrincipal();
        } else {
            this.user = null;
        }
    }

    public boolean isLogin() {
        return user != null;
    }

    public boolean isLogout() {
        return !isLogin();
    }

    public SiteUser getSiteUser() {
        if (isLogout()) {
            return null;
        }

        if (siteUser == null) {
            siteUser = userService.findByUsername(getLoginedSiteUserUsername());
        }

        return siteUser;
    }


    private String getLoginedSiteUserUsername() {
        if (isLogout()) return null;

        return user.getUsername();
    }

    public List<Category> getCategory() {

        List<Category> categoryList = new ArrayList<>();

        for (int i = 1; i <= 7; i++) {
            categoryList.add(this.categoryService.findById((long) i));
        }

        return categoryList;
    }


}

