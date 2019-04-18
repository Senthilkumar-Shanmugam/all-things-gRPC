package com.exmaple.grpc.greeting.client;

import com.proto.dummy.DummyServiceGrpc.DummyServiceBlockingStub;
import com.proto.dummy.DummyServiceGrpc;
import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {
    public static void main(String[] args) {
        System.out.println("Hi from Greetings client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",
                                                                  50051)
                                                       .usePlaintext()//to disable default
                                                          .build();

       // DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);

       /* DummyServiceGrpc.DummyServiceFutureStub asyncClient =
                DummyServiceGrpc.newFutureStub(channel);*/

        GreetServiceGrpc.GreetServiceBlockingStub synGreetClient =
                GreetServiceGrpc.newBlockingStub(channel);

        Greeting greeting = Greeting.newBuilder()
                                    .setFirstName("Clapton")
                                     .setLastName("Eric").build();
        GreetRequest greetRequest = GreetRequest.newBuilder()
                                                .setGreeting(greeting)
                                                .build();

        //unary API call
        /**
        GreetResponse greetResponse = synGreetClient.greet(greetRequest);
        System.out.println(greetResponse.getResult());**/

        //server streaming api call in blocking call
        GreetManyTimesRequest greetManyTimesRequest =
                GreetManyTimesRequest.newBuilder().setGreeting(Greeting.newBuilder().setFirstName("Gallahar").build()).build();
        synGreetClient.greetManyTimes(greetManyTimesRequest).forEachRemaining(greetManyTimesResponse -> {
            System.out.println(greetManyTimesResponse.getResult());
        });

        //shutdown
        System.out.println("shutting down chaneel");
        channel.shutdown();



    }
}
