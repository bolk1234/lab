package by.Danik.lab.services;

import by.Danik.lab.models.RequestCounter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class CounterService {

    @Autowired
    private RequestCounter requestCounter;      //счётчик запросов к сервису поиск фильмов по названию

    public int getCurrentCount() {
        return requestCounter.getCount();
    }
}
