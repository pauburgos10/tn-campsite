package com.mytruenorthproject.campsite;

import com.google.common.collect.Lists;
import com.mytruenorthproject.campsite.model.Campsite;
import com.mytruenorthproject.campsite.model.Reservation;
import com.mytruenorthproject.campsite.repository.CampsiteRepository;
import com.mytruenorthproject.campsite.service.base.CampsiteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MainApplicationTest {


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CampsiteRepository campsiteRepository;

    @Test
    public void whenFindByName_thenReturnCampsite() {
        // given
        String name = "nuevo campsite";
        Campsite campsite = Campsite.builder().name(name).build();
        entityManager.persist(campsite);
        entityManager.flush();

        // when
        Campsite found = campsiteRepository.findByName(name);

        // then
        assertEquals(found.getName(),campsite.getName());
    }


}