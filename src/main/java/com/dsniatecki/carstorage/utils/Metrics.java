package com.dsniatecki.carstorage.utils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

public class Metrics {

    private Metrics(){
    }

    public static <T> Mono<T> recorded(Mono<T> mono, TimeRecorder timeRecorder) {
        long start = System.currentTimeMillis();
        return mono.doFinally(result -> timeRecorder.record(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS));
    }

    public static <T> Flux<T> recorded(Flux<T> flux, TimeRecorder timeRecorder) {
        long start = System.currentTimeMillis();
        return flux.doFinally(result -> timeRecorder.record(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS));
    }
}
