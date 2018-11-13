package com.mytruenorthproject.campsite;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author Paula
 */
@SpringBootApplication
//public class MainApplication implements ApplicationRunner {
public class MainApplication {
    /*@Autowired
    private SlotsDataLoaderServiceImpl slotsDataLoader;*/

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    /*@Override
    public void run(ApplicationArguments args) throws Exception {
        slotsDataLoader.loadCampsiteDataForDemo();
        slotsDataLoader.loadSlotsDataForDemo();
    }*/
    
}
