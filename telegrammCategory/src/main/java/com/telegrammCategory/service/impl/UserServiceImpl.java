package com.telegrammCategory.service.impl;

import com.telegrammCategory.model.UserState;
import com.telegrammCategory.service.UserService;
import org.jvnet.hk2.annotations.Service;

/**  Сервис Категорий  */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserState getUserState(long chatId) {
        return null;
    }

    @Override
    public void updateUserState(UserState userState) {

    }

    @Override
    public int getLevelUserState(long chatId) {
        return 0;
    }

    @Override
    public void saveUserLastAction(String action,long chatId) {

    }
}
