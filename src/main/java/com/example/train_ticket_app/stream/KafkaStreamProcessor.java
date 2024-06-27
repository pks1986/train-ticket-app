package com.example.train_ticket_app.stream;

import com.example.train_ticket_app.model.Ticket;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

@Service
public class KafkaStreamProcessor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_BUILDER_BEAN_NAME)
    public KStream<String, String> kStream(StreamsBuilderFactoryBean streamsBuilderFactoryBean) throws Exception {
        KStream<String, String> stream = streamsBuilderFactoryBean.getObject().stream("ticket-purchases");

        stream.map((key, value) -> {
                    try {
                        Ticket ticket = objectMapper.readValue(value, Ticket.class);
                        return new KeyValue<>(key, ticket.getUser().getEmail());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }).filter((key, value) -> value != null)
                .to("processed-ticket-purchases");

        return stream;
    }
}
