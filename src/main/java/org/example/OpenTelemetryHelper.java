package org.example;

import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.TracerSdkProvider;
import io.opentelemetry.trace.Span;
import io.opentelemetry.trace.Status;
import io.opentelemetry.trace.Tracer;

import java.util.concurrent.Callable;

public class OpenTelemetryHelper {

    private static Tracer tracer;

    // this will reside in a Quarkus extension
    // initializes the tracer that includes our processor that computes metrics from spans
    public static void initTracer() {
        TracerSdkProvider tracerProvider = OpenTelemetrySdk.getTracerProvider();
        // FIXME: on reload in devmode, the processor never gets removed so here it registers a duplicate
        // the Quarkus extension will need to make sure it is only registered once
        tracerProvider.addSpanProcessor(new MicrometerSpanProcessor());
        tracer = tracerProvider.get("instrumentation-library-name");
    }

    // wraps a Callable with a span
    public static <T> T traced(String spanName, Callable<T> codeBlock) throws Exception {
        Span span = tracer.spanBuilder(spanName).startSpan();
        try (Scope scope = tracer.withSpan(span)) {
            return codeBlock.call();
        } catch (Throwable t) {
            Status status = Status.UNKNOWN.withDescription("error!");
            span.setStatus(status);
            throw t;
        } finally {
            span.end();
        }
    }
}
