package com.telegrammCategory.service.impl;



import com.telegrammCategory.model.UserState;
import com.telegrammCategory.repository.UserStateRepository;
import com.telegrammCategory.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**  Сервис Категорий  */
@Slf4j
@Component
@Service
public class UserServiceImpl implements UserService {
    private UserStateRepository userStateRepository;

    public UserServiceImpl(UserStateRepository userStateRepository) {
        this.userStateRepository = userStateRepository;
    }

    @Override
    public UserState getUserState(long chatId) {
        return  userStateRepository.findById(chatId);
    }

    @Override
    public void updateUserState(UserState userState,long chatId) {
        userStateRepository.updateUserState(userState,chatId);
    }

    @Override
    public int getLevelUserState(long chatId) {
        return         userStateRepository.getLevelUserState(chatId);
    }

    @Override
    public void saveUserLastAction(String action,long chatId) {
        userStateRepository.saveUserLastAction(action, chatId);
    }

    @Override
    public void setLevelUserState(long chatId,int level) {
        userStateRepository.setLevelUserState(chatId,level);
    }
}
