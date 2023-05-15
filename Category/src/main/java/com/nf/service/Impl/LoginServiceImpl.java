package com.nf.service.Impl;

import com.nf.dao.Impl.LoginDaoImpl;
import com.nf.dao.LoginDao;
import com.nf.entity.UserEntity;
import com.nf.service.LoginService;

/**
 * @ClassName LoginServiceImpl
 * @Author ZL
 * @Date 2023/5/12 9:13
 * @Version 1.0
 * @Explain
 **/
public class LoginServiceImpl implements LoginService {
    LoginDao dao=new LoginDaoImpl();
    @Override
    public boolean login(UserEntity userEntity)  {

        return dao.login(userEntity);
    }
}
