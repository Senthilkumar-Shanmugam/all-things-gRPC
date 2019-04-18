package com.exmaple.grpc.greeting.client;

import com.proto.dummy.DummyServiceGrpc.DummyServiceBlockingStub;
import com.proto.dummy.DummyServiceGrpc;
import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
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
        GreetResponse greetResponse = synGreetClient.greet(greetRequest);

        System.out.println(greetResponse.getResult());

        //shutdown
        System.out.println("shutting down chaneel");
        channel.shutdown();



    }
}
