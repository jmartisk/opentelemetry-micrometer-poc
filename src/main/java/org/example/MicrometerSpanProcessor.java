package org.example;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.opentelemetry.sdk.trace.ReadableSpan;
import io.opentelemetry.sdk.trace.SpanProcessor;

import java.util.concurrent.TimeUnit;

// TODO: this will be in the micrometer extension, in SmallRye OpenTelemetry, or in Quarkus OT extension?
public class MicrometerSpanProcessor implements SpanProcessor {

    private final MeterRegistry registry;
    private final String SPAN_TIMER_METRIC_NAME = "spans";

    public MicrometerSpanProcessor() {
        registry = Metrics.globalRegistry;
    }

    @Override
    public void onStart(ReadableSpan readableSpan) {
    }

    @Override
    public boolean isStartRequired() {
        return false;
    }

    @Override
    public void onEnd(ReadableSpan readableSpan) {
        registry.timer(SPAN_TIMER_METRIC_NAME, Tags.of("name", readableSpan.getName()))
                .record(readableSpan.getLatencyNanos(), TimeUnit.NANOSECONDS);
    }

    @Override
    public boolean isEndRequired() {
        return true;
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void forceFlush() {
    }
}
