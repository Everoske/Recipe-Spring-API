package com.everoske.RecipeDemo.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;
import org.springframework.boot.json.JsonWriter;

/**
 * Parses Logs into a JSON Format
 */
public class JsonLogger extends LayoutBase<ILoggingEvent> {
    private final JsonWriter<ILoggingEvent> writer = JsonWriter.<ILoggingEvent>of((members) -> {
        members.add("Timestamp", ILoggingEvent::getTimeStamp);
        members.add("Level", ILoggingEvent::getLevel);
        members.add("Logger", ILoggingEvent::getLoggerName);
        members.add("Sequence", ILoggingEvent::getSequenceNumber);
        members.add("Thread", ILoggingEvent::getThreadName);
        members.add("Class", ILoggingEvent::getClass);
        members.add("Message", ILoggingEvent::getFormattedMessage);
    }).withNewLineAtEnd();

    @Override
    public String doLayout(ILoggingEvent event) {
        return this.writer.writeToString(event);
    }
}
