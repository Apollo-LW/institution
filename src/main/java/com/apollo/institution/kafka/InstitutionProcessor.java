package com.apollo.institution.kafka;

import com.apollo.institution.model.Institution;
import com.apollo.institution.model.InstitutionUser;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class InstitutionProcessor {

    @Value("${institution.kafka.store}")
    private String institutionStateStoreName;
    @Value("${user.kafka.store}")
    private String institutionUserStateStoreName;

    @Bean
    public Function<KStream<String , Institution> , KTable<String , Institution>> institutionStateProcessor() {
        return institutionKStream -> {
            institutionKStream
                    .flatMap((institutionId , institution) -> institution.getInstitutionMembers().stream().map(memberId -> new KeyValue<String , Institution>(memberId , institution)).collect(Collectors.toSet()))
                    .groupByKey(Grouped.with(Serdes.String() , CustomSerdes.institutionSerdes()))
                    .aggregate(InstitutionUser::new , (key , value , aggregate) -> aggregate.addInstitution(value) , Materialized.with(Serdes.String() , CustomSerdes.institutionUserSerde()))
                    .toStream()
                    .groupByKey()
                    .reduce((institutionUser , updatedInstitutionUser) -> updatedInstitutionUser , Materialized.as(this.institutionUserStateStoreName));

            return institutionKStream
                    .groupByKey()
                    .reduce((institution , updatedInstitution) -> updatedInstitution , Materialized.as(this.institutionStateStoreName));
        };
    }

}
