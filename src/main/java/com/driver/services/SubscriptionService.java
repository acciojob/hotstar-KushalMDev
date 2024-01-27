package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        Subscription subscription=new Subscription();
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setStartSubscriptionDate(new Date());
        int userId=subscriptionEntryDto.getUserId();
        Optional<User> user=userRepository.findById(userId);
        int totalAmountPaid=0;
        if(user.isPresent()){
        if(subscriptionEntryDto.getSubscriptionType().equals("BASIC")){
            totalAmountPaid=500+200*subscriptionEntryDto.getNoOfScreensRequired();
        }
        else if(subscriptionEntryDto.getSubscriptionType().equals("PRO")){
            totalAmountPaid=800+250*subscriptionEntryDto.getNoOfScreensRequired();
        }
        else{
            totalAmountPaid=1000+350*subscriptionEntryDto.getNoOfScreensRequired();
        }
        subscription.setTotalAmountPaid(totalAmountPaid);
        subscription.setUser(user.get());
        userRepository.save(user.get());
    }
        return totalAmountPaid;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        Optional<User> user2=userRepository.findById(userId);
        if(user2.isEmpty()){
            return 0;
        }
        User user=user2.get();
        Subscription subscription=user.getSubscription();
        int noOfScreens=subscription.getNoOfScreensSubscribed();
        int currentAmount=subscription.getTotalAmountPaid();
        SubscriptionType subscriptionType=subscription.getSubscriptionType();
        if(subscriptionType.equals(SubscriptionType.ELITE)){
            throw new Exception("Already the best Subscription");
        }
        else if(subscriptionType.equals(SubscriptionType.BASIC)){
            int upgradeSubscription=800+250*noOfScreens;
            subscription.setSubscriptionType(SubscriptionType.PRO);
            subscription.setTotalAmountPaid(upgradeSubscription);
            subscriptionRepository.save(subscription);
            return upgradeSubscription-currentAmount;
        }
        else{
            int upgradeSubscription=1000+350*noOfScreens;
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            subscription.setTotalAmountPaid(upgradeSubscription);
            subscriptionRepository.save(subscription);
            return upgradeSubscription-currentAmount;
        }
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        List<Subscription> list=subscriptionRepository.findAll();
        int totalRevenue=0;
        for(Subscription sub:list){
            
            totalRevenue+=sub.getTotalAmountPaid();
        }
        return totalRevenue;
    }

}
