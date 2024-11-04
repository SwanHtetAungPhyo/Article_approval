package com.swanhtetaungphyo.article_approval.services;

import com.swanhtetaungphyo.article_approval.contract.UserServices;
import com.swanhtetaungphyo.article_approval.dto.UserDto;
import com.swanhtetaungphyo.article_approval.models.User;
import com.swanhtetaungphyo.article_approval.repo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserServices {
    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public boolean CreateUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        try{
            userRepository.save(user);
            return true;
        }catch (EntityNotFoundException ex){
            log.atInfo().log(ex.getMessage());
            return false;
        }
    }


}
