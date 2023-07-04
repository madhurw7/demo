package com.testing.demo.service;

import com.testing.demo.objects.Input;
import com.testing.demo.objects.Result;
import com.testing.demo.repository.ResultRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class ResultService {

    ResultRepository resultRepository;

    private static Random random = new Random();

    public Optional<Result> addToCache1(long whId) {
        Input input = new Input(whId, Math.abs(random.nextLong()));
        return resultRepository.getResultFromCache1(input);
    }

    public void clearCache1() {
        resultRepository.evictAllCache1Values();
    }

    public Optional<Result> addToCache2(long whId) {
        Input input = new Input(whId, Math.abs(random.nextLong()));
        return resultRepository.getResultFromCache2(input);
    }

    public void clearCache2() {
        resultRepository.evictAllCache2Values();
    }

    public Optional<Result> addToCache3(long whId, long pin) {
        Input input = new Input(whId, pin);
        return resultRepository.getResultFromCache3viaCacheManager(input);
    }

    public void clearCache3() {
        resultRepository.evictAllCache3Values();
    }

}
