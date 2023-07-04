package com.testing.demo.controller;

import com.testing.demo.objects.Result;
import com.testing.demo.service.ResultService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
public class ResultController {

    ResultService resultService;

    @GetMapping("/cache1/{id}")
    public Optional<Result> getFromCache1(@PathVariable("id") long whId) {
        return resultService.addToCache1(whId);
    }

    @PostMapping("/cache1/")
    public Optional<String> evictCache1() {
        resultService.clearCache1();
        return Optional.of("Done");
    }

    @GetMapping("/cache2/{id}")
    public Optional<Result> getFromCache2(@PathVariable("id") long whId) {
        return resultService.addToCache2(whId);
    }

    @PostMapping("/cache2/")
    public Optional<String> evictCache2() {
        resultService.clearCache2();
        return Optional.of("Done");
    }

    @GetMapping("/cache3/{id}/pin/{pin}")
    public Optional<Result> getFromCache3(@PathVariable("id") long whId, @PathVariable("pin") long pin) {
        return resultService.addToCache3(whId, pin);
    }

    @PostMapping("/cache3/")
    public Optional<String> evictCache3() {
        resultService.clearCache3();
        return Optional.of("Done");
    }

}
