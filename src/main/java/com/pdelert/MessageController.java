package com.pdelert;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Path("/kafka")
public class MessageController {

    @Inject
    @Channel("events")
    Emitter<String> emitter;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public CompletionStage<Response> sendMsg() {
        UUID uuid = UUID.randomUUID();
        CompletableFuture<Response> response = new CompletableFuture<>();
        Message<String> msg = Message
                .of(uuid.toString())
                .withAck(() -> {
                    response.complete(Response.ok().entity(uuid).build());
                    return CompletableFuture.completedFuture(null);
                })
                .withNack(reason -> {
                    response.complete(Response.serverError().build());
                    return CompletableFuture.completedFuture(null);
                });
        emitter.send(msg);
        return response;
    }
}