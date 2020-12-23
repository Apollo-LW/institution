package com.apollo.institution.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.internals.ConsumerFactory;
import reactor.kafka.receiver.internals.DefaultKafkaReceiver;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.internals.DefaultKafkaSender;
import reactor.kafka.sender.internals.ProducerFactory;

import java.util.Properties;

@Configuration
public class KafkaConfiguration {

    @Value("${institution.kafka.topic}")
    private String institutionTopicName;
    @Value("${institution.kafka.partitions}")
    private Integer institutionPartition;
    @Value("${institution.kafka.replicas}")
    private Short institutionReplicas;
    @Value("${institution.kafka.retention}")
    private String institutionRetentionPeriod;
    @Value("${institution.kafka.server}")
    private String bootstrapServer;
    @Value("${institution.kafka.acks}")
    private String acks;
    @Value("${institution.kafka.retries}")
    private String numberOfRetries;
    @Value("${institution.kafka.requestimeout}")
    private String requestTimeout;
    @Value("${institution.kafka.batch}")
    private String batchSize;
    @Value("${institution.kafka.linger}")
    private String linger;
    @Value("${institution.kafka.max-in-flight}")
    private String maxInFlight;
    @Value("${institution.kafka.client-id}")
    private String clientId;
    @Value("${institution.kafka.group-id}")
    private String groupId;
    @Value("${institution.kafka.offset}")
    private String offsetConfig;

    @Bean
    NewTopic createInstitutionTopic() {
        return TopicBuilder
                .name(this.institutionTopicName)
                .partitions(this.institutionPartition)
                .replicas(this.institutionReplicas)
                .config(TopicConfig.RETENTION_MS_CONFIG , this.institutionRetentionPeriod)
                .build();
    }

    @Bean
    KafkaSender institutionKafkaSender() {
        final Properties institutionProperties = new Properties();
        institutionProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG , this.bootstrapServer);
        institutionProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG , StringSerializer.class);
        institutionProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG , JsonSerializer.class);
        institutionProperties.put(ProducerConfig.ACKS_CONFIG , this.acks);

        institutionProperties.put(ProducerConfig.RETRIES_CONFIG , this.numberOfRetries);
        institutionProperties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG , this.requestTimeout);
        institutionProperties.put(ProducerConfig.BATCH_SIZE_CONFIG , this.batchSize);
        institutionProperties.put(ProducerConfig.LINGER_MS_CONFIG , this.linger);
        institutionProperties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION , this.maxInFlight);

        return new DefaultKafkaSender<>(ProducerFactory.INSTANCE , SenderOptions.create(institutionProperties));
    }

    @Bean
    KafkaReceiver institutionKafkaReceiver() {
        final Properties institutionProperties = new Properties();
        institutionProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG , this.bootstrapServer);
        institutionProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG , StringDeserializer.class);
        institutionProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG , JsonDeserializer.class);
        institutionProperties.put(ConsumerConfig.CLIENT_ID_CONFIG , this.clientId);
        institutionProperties.put(ConsumerConfig.GROUP_ID_CONFIG , this.groupId);
        institutionProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG , true);
        institutionProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG , this.offsetConfig);

        return new DefaultKafkaReceiver(ConsumerFactory.INSTANCE , ReceiverOptions.create(institutionProperties));
    }


}
