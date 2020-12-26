package com.apollo.institution.kafka;

import com.apollo.institution.model.Institution;
import com.apollo.institution.model.InstitutionUser;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class CustomSerdes {

    static public final class InstitutionSerdes extends Serdes.WrapperSerde<Institution> {
        public InstitutionSerdes() {
            super(new JsonSerializer<>() , new JsonDeserializer<>(Institution.class));
        }
    }

    static public final class InstitutionUserSerdes extends Serdes.WrapperSerde<InstitutionUser> {
        public InstitutionUserSerdes() {
            super(new JsonSerializer<>() , new JsonDeserializer<>(InstitutionUser.class));
        }
    }

    public static Serde<Institution> institutionSerdes() {
        return new CustomSerdes.InstitutionSerdes();
    }

    public static Serde<InstitutionUser> institutionUserSerde() {
        return new CustomSerdes.InstitutionUserSerdes();
    }

}
