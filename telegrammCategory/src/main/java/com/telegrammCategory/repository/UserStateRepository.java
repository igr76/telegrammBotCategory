package com.telegrammCategory.repository;


import com.telegrammCategory.model.UserState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserStateRepository {
    Map<Long, UserState> userMap = new HashMap<>();

    public void updateUserState(UserState userState,long chatId) {
        userMap.put(chatId, userState);
    }

    public int getLevelUserState(long chatId) {
        if (userMap.get(chatId) == null) {
            return 1;
        } else {
            System.out.println(userMap);return userMap.get(chatId).getLevel();
        }
    }

    public void saveUserLastAction(String action,long chatId) {
        UserState userState = findById(chatId);
        userState.setLastAction(action);
        userMap.put(chatId,userState);
    }

    public UserState findById(long chatId) {

        return userMap.get(chatId);


    }

    public void setLevelUserState(long chatId, int level) {
        UserState userState = userMap.get(chatId);
        userState.setLevel(level);
        userMap.put(chatId,userState);
    }
}
