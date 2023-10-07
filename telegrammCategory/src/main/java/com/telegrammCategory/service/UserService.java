package com.telegrammCategory.service;

import com.telegrammCategory.model.UserState;

public interface UserService {
    UserState getUserState(long chatId);
    void updateUserState(UserState userState,long chatId);
    int getLevelUserState(long chatId);
    void saveUserLastAction(String action,long chatId);

    void setLevelUserState(long chatId, int level);
}
