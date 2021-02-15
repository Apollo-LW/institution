package com.apollo.institution.kafka.processor;

import com.apollo.institution.kafka.CustomSerdes;
import com.apollo.institution.model.Institution;
import com.apollo.institution.model.InstitutionUser;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class InstitutionUserProcessor {

    @Value("${user.kafka.store}")
    private String institutionUserStateStoreName;

    @Bean
    public Function<KStream<String, Institution>, KTable<String, InstitutionUser>> userInstitutionProcessorState() {
        return institutionKStream -> institutionKStream
                .flatMap((institutionId , institution) -> institution.getAllInstitutionMembers().stream().map(userId -> new KeyValue<String, Institution>(userId , institution)).collect(Collectors.toSet()))
                .groupByKey(Grouped.with(Serdes.String() , CustomSerdes.institutionSerdes()))
                .aggregate(InstitutionUser::new ,
                        (userId , institution , institutionUser) -> institutionUser.addInstitution(institution.getInstitutionId()) ,
                        Materialized.with(Serdes.String() , CustomSerdes.institutionUserSerde()))
                .toStream()
                .groupByKey()
                .reduce((institutionUser , updatedInstitutionUser) -> updatedInstitutionUser , Materialized.as(this.institutionUserStateStoreName));
    }

}
