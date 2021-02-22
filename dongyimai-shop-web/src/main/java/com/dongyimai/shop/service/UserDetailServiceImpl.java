package com.dongyimai.shop.service;

import com.dongyimai.pojo.TbSeller;
import com.dongyimai.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class UserDetailServiceImpl implements UserDetailsService {
    //获取业务层对象 目的：查询数据库
    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //定义角色的权限
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority("ROLE_SELLER"));


        //1、查询数据库
        TbSeller seller = sellerService.findOne(username);
        //2、用户存在 且 审核通过 才允许登录
        if(seller!=null && "1".equals(seller.getStatus())){
            return new User(username,seller.getPassword(),list);
        }else{
            return null;
        }

    }
}
