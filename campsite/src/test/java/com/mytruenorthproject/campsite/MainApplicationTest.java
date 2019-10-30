package com.mytruenorthproject.campsite;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainApplicationTest {


    @Autowired
    private TestRestTemplate restTemplate;

    /*@Test
    public void testAvailabilities() {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("fromDate", "2017-11-02");
        uriParams.put("toDate", "2017-11-04");

        ResponseEntity<List> entity = restTemplate.getForEntity("/campsite/availability?start_Date={fromDate}&end_Date={toDate}", List.class, uriParams);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        List dates = entity.getBody();
        assertEquals(3, dates.size());
        assertTrue(dates.contains("2017-11-02"));
        assertTrue(dates.contains("2017-11-03"));
        assertTrue(dates.contains("2017-11-04"));

        entity = restTemplate.getForEntity("/campsite/availabilities?fromDate={fromDate}", List.class, uriParams);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        dates = entity.getBody();
        assertEquals(30, dates.size());
        assertEquals("2017-11-02", dates.get(0));
        assertEquals("2017-12-01", dates.get(dates.size() - 1));
    }*/


    @Test
    public void testBasicConcurrency() throws Exception {
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        LocalDate dayAfterTomorrow = now.plusDays(2);
        LocalDate threeDaysFromNow = now.plusDays(3);
        LocalDate fourDaysFromNow = now.plusDays(4);

       /* HttpEntity<CampsiteBookingInfo> requestEntity1 = buildRequestEntity("email1", tomorrow, threeDaysFromNow);
        HttpEntity<CampsiteBookingInfo> requestEntity2 = buildRequestEntity("email2", tomorrow, dayAfterTomorrow);
        HttpEntity<CampsiteBookingInfo> requestEntity3 = buildRequestEntity("email3", tomorrow, tomorrow);
        HttpEntity<CampsiteBookingInfo> requestEntity4 = buildRequestEntity("email4", fourDaysFromNow, fourDaysFromNow);

        List<HttpEntity<CampsiteBookingInfo>> requests = Lists.newArrayList(requestEntity1, requestEntity2, requestEntity3, requestEntity4);
        Collections.shuffle(requests);

        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Future<ResponseEntity<BookingStatusInfo>>> futures = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            futures.add(executeRequest(executor, requests.get(0)));
            futures.add(executeRequest(executor, requests.get(1)));
            futures.add(executeRequest(executor, requests.get(2)));
            futures.add(executeRequest(executor, requests.get(3)));
        }

        int totalSuccess = 0;
        int totalFailures = 0;
        for (Future<ResponseEntity<BookingStatusInfo>> future : futures) {
            ResponseEntity<BookingStatusInfo> responseEntity = future.get();
            BookingStatusInfo.Status status = responseEntity.getBody().getStatus();
            if (status == BookingStatusInfo.Status.CREATED) {
                totalSuccess++;
            } else if (status == BookingStatusInfo.Status.ERROR && responseEntity.getStatusCode() == HttpStatus.CONFLICT){
                totalFailures++;
            }
        }

        assertEquals(2, totalSuccess);
        assertEquals(38, totalFailures);*/
    }

    private HttpEntity<String> buildRequestEntity(String email, LocalDate arrivalDate, LocalDate departureDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonReservation = "{\n" +
                "  \"email\": \"john\",\n" +
                "  \"fullName\": \"doe\",\n" +
                "  \"arrivalDate\": \""+ arrivalDate +"\",\n" +
                "  \"departureDate\": \""+ departureDate +"\"\n" +
                "}";

        return new HttpEntity<>(jsonReservation, headers);
    }


}