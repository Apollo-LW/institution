package com.apollo.institution.kafka;

import com.apollo.institution.model.Institution;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.util.Optional;

@Service
@CommonsLog
@RequiredArgsConstructor
public class KafkaService {

    @Value("${institution.kafka.topic}")
    private String institutionTopicName;
    private final KafkaSender<String , Institution> institutionKafkaSender;

    public Mono<Optional<Institution>> sendInstitutionRecord(Mono<Institution> institutionMono) {
        return institutionMono.flatMap(institution -> this.institutionKafkaSender
                .send(Mono.just(SenderRecord.create(new ProducerRecord<>(this.institutionTopicName , institution.getInstitutionId() , institution) , 1)))
                .next()
                .doOnNext(log::info)
                .doOnError(log::error)
                .map(integerSenderResult -> integerSenderResult.exception() == null ? Optional.of(institution) : Optional.empty()));
    }

}
