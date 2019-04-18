package com.exmaple.grpc.greeting.client;

import com.proto.dummy.DummyServiceGrpc.DummyServiceBlockingStub;
import com.proto.dummy.DummyServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {
    public static void main(String[] args) {
        System.out.println("Hi from Greetings client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress(  "localhost",
                                                                  50051).build();

        DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);

       /* DummyServiceGrpc.DummyServiceFutureStub asyncClient =
                DummyServiceGrpc.newFutureStub(channel);*/

       //do something

        //shutdown
        System.out.println("shutting down chaneel");
        channel.shutdown();



    }
}
