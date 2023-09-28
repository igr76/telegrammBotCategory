package com.telegrammCategory.service;

import com.telegrammCategory.model.UserState;

public interface UserService {
    UserState getUserState(long chatId);
    void updateUserState(UserState userState);
    int getLevelUserState(long chatId);
    void saveUserLastAction(String action,long chatId);
}
