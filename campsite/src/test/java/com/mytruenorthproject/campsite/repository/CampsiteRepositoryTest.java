package com.mytruenorthproject.campsite.repository;

import com.mytruenorthproject.campsite.model.Campsite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CampsiteRepositoryTest {

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