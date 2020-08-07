package org.example.rest;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import org.example.OpenTelemetryHelper;

import javax.enterprise.event.Observes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import static org.example.OpenTelemetryHelper.traced;

@Path("/")
public class HelloWorld {

    @Startup
    public void init(@Observes StartupEvent ev) {
        OpenTelemetryHelper.initTracer();
    }

    @GET
    @Path("/")
    public String hello() throws Exception {
        return traced("sayHello", () -> {
            return traced("sayHelloInner", () -> "hello1");
        });
    }

}
