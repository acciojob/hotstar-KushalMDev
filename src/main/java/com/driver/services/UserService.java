package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){
        User newUser=userRepository.save(user);
        return newUser.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository

        List<WebSeries> webSeries=webSeriesRepository.findAll();
        User user=userRepository.findById(userId).get();
        SubscriptionType subscriptiontType=user.getSubscription().getSubscriptionType();
        int age=user.getAge();
        int count=0;
        for(WebSeries webSeries2:webSeries){
            if(age>=webSeries2.getAgeLimit() && webSeries2.getSubscriptionType().equals(subscriptiontType)){
                count++;
            }
        }
        return count;
    }


}
