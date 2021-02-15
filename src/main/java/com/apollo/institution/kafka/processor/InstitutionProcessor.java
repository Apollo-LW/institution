package com.apollo.institution.kafka.processor;

import com.apollo.institution.model.Institution;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class InstitutionProcessor {

    @Value("${institution.kafka.store}")
    private String institutionStateStoreName;

    @Bean
    public Function<KStream<String, Institution>, KTable<String, Institution>> institutionProcessorState() {
        return institutionKStream -> institutionKStream
                .groupByKey()
                .reduce((institution , updatedInstitution) -> updatedInstitution , Materialized.as(this.institutionStateStoreName));
    }

}
