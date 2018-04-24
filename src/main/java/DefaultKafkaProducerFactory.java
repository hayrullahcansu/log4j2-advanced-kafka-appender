import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.logging.log4j.core.appender.mom.kafka.KafkaProducerFactory;

/**
 * Creates <a href="https://kafka.apache.org/">Apache Kafka</a> {@link Producer} instances.
 */
public class DefaultKafkaProducerFactory implements KafkaProducerFactory {

    /**
     * Creates a new Kafka Producer from the given configuration properties.
     *
     * @param config <a href="https://kafka.apache.org/documentation.html#producerconfigs">Kafka Producer configuration
     *               properties.</a>
     * @return a new Kafka {@link Producer}.
     */
    @Override
    public org.apache.kafka.clients.producer.Producer<byte[], byte[]> newKafkaProducer(final Properties config) {
        return new KafkaProducer<>(config);
    }

}