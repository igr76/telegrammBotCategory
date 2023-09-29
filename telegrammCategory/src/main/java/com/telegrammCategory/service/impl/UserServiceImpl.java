package com.telegrammCategory.service.impl;

import com.telegrammCategory.exception.ElemNotFound;
import com.telegrammCategory.model.UserState;
import com.telegrammCategory.repository.UserStateRepository;
import com.telegrammCategory.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**  Сервис Категорий  */
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserStateRepository userStateRepository;


    @Override
    public UserState getUserState(long chatId) {
        try {
           return userStateRepository.findById(chatId).orElseThrow(ElemNotFound::new);
        }catch (RuntimeException e){return null;}
    }

    @Override
    public void updateUserState(UserState userState) {
        userStateRepository.save(userState);
    }

    @Override
    public int getLevelUserState(long chatId) {
        
        return userStateRepository.findLevelById(chatId);
    }

    @Override
    public void saveUserLastAction(String action,long chatId) {
        UserState userState = new UserState();
        userState.setId(chatId);
        userState.setLastAction(action);
        userStateRepository.save(userState);
    }
}
