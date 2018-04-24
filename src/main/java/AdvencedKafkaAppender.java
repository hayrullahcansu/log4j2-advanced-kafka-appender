import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.layout.SerializedLayout;
import org.apache.logging.log4j.core.util.StringEncoder;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;



/**
 * Sends log events to an Apache Kafka topic.
 */
@Plugin(name = "AdvencedKafkaAppender", category = Node.CATEGORY, elementType = Appender.ELEMENT_TYPE, printObject = true)
public final class AdvencedKafkaAppender extends AbstractAppender {

    /**
     * Builds AdvencedKafkaAppender instances.
     * @param <B> The type to build
     */
    public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B>
            implements org.apache.logging.log4j.core.util.Builder<AdvencedKafkaAppender> {

        @PluginAttribute("topic")
        private String topic;

        @PluginAttribute(value = "syncSend", defaultBoolean = true)
        private boolean syncSend;

        @PluginElement("Properties")
        private Property[] properties;

        @SuppressWarnings("resource")
        @Override
        public AdvencedKafkaAppender build() {
            final AdvencedKafkaManager advencedKafkaManager = new AdvencedKafkaManager(getConfiguration().getLoggerContext(), getName(), topic, syncSend, properties);
            return new AdvencedKafkaAppender(getName(), getLayout(), getFilter(), isIgnoreExceptions(), advencedKafkaManager);
        }

        public String getTopic() {
            return topic;
        }

        public Property[] getProperties() {
            return properties;
        }

        public B setTopic(String topic) {
            this.topic = topic;
            return asBuilder();
        }

        public B setSyncSend(boolean syncSend) {
            this.syncSend = syncSend;
            return asBuilder();
        }

        public B setProperties(Property[] properties) {
            this.properties = properties;
            return asBuilder();
        }
    }

    @Deprecated
    public static AdvencedKafkaAppender createAppender(
            @PluginElement("Layout") final Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter,
            @Required(message = "No name provided for AdvencedKafkaAppender") @PluginAttribute("name") final String name,
            @PluginAttribute(value = "ignoreExceptions", defaultBoolean = true) final boolean ignoreExceptions,
            @Required(message = "No topic provided for AdvencedKafkaAppender") @PluginAttribute("topic") final String topic,
            @PluginElement("Properties") final Property[] properties,
            @PluginConfiguration final Configuration configuration) {
        final AdvencedKafkaManager advencedKafkaManager = new AdvencedKafkaManager(configuration.getLoggerContext(), name, topic, true, properties);
        return new AdvencedKafkaAppender(name, layout, filter, ignoreExceptions, advencedKafkaManager);
    }

    /**
     * Creates a builder for a AdvencedKafkaAppender.
     * @return a builder for a AdvencedKafkaAppender.
     */
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }

    private final AdvencedKafkaManager manager;

    private AdvencedKafkaAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter,
                                  final boolean ignoreExceptions, final AdvencedKafkaManager manager) {
        super(name, filter, layout, ignoreExceptions);
        this.manager = Objects.requireNonNull(manager, "manager");
    }

    @Override
    public void append(final LogEvent event) {
        System.out.println("MESAJ GELDI KNK");
        if (event.getLoggerName() != null && event.getLoggerName().startsWith("org.apache.kafka")) {
            LOGGER.warn("Recursive logging from [{}] for appender [{}].", event.getLoggerName(), getName());
        } else {
            try {
                final Layout<? extends Serializable> layout = getLayout();
                byte[] data;
                if (layout != null) {
                    if (layout instanceof SerializedLayout) {
                        final byte[] header = layout.getHeader();
                        final byte[] body = layout.toByteArray(event);
                        data = new byte[header.length + body.length];
                        System.arraycopy(header, 0, data, 0, header.length);
                        System.arraycopy(body, 0, data, header.length, body.length);
                    } else {
                        data = layout.toByteArray(event);
                    }
                } else {
                    data = StringEncoder.toBytes(event.getMessage().getFormattedMessage(), StandardCharsets.UTF_8);
                }
                manager.send(data);
            } catch (final Exception e) {
                LOGGER.error("Unable to write to Kafka [{}] for appender [{}].", manager.getName(), getName(), e);
                throw new AppenderLoggingException("Unable to write to Kafka in appender: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void start() {
        super.start();
        manager.startup();
    }

    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        setStopping();
        boolean stopped = super.stop(timeout, timeUnit, false);
        stopped &= manager.stop(timeout, timeUnit);
        setStopped();
        return stopped;
    }

    @Override
    public String toString() {
        return "AdvencedKafkaAppender{" +
                "name=" + getName() +
                ", state=" + getState() +
                ", topic=" + manager.getTopic() +
                '}';
    }
}