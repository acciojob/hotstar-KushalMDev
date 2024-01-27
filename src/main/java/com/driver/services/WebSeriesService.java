package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo
        ProductionHouse productionHouse=productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId()).get();
        WebSeries web=webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName());
        if(web==null){
            throw new Exception("Series is already present");
        }
        else{
            WebSeries webSeries=new WebSeries(webSeriesEntryDto.getSeriesName(),webSeriesEntryDto.getAgeLimit(),webSeriesEntryDto.getRating(),webSeriesEntryDto.getSubscriptionType());
            webSeries.setProductionHouse(productionHouse);
            List<WebSeries> webSeriesList=productionHouse.getWebSeriesList();
            int size=webSeriesList.size();
            int total=0;
            for(WebSeries webSeries2:webSeriesList){
                total+=webSeries2.getRating();
            }
            double avg=(double)total/(double)size;
            productionHouse.setRatings(avg);
            productionHouse.getWebSeriesList().add(webSeries);
            WebSeries w=webSeriesRepository.save(webSeries);
            return w.getId();
        }
        
    }

}
