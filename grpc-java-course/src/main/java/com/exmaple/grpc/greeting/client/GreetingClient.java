package com.exmaple.grpc.greeting.client;

import com.proto.dummy.DummyServiceGrpc.DummyServiceBlockingStub;
import com.proto.dummy.DummyServiceGrpc;
import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {


    public static void main(String[] args) {
        System.out.println("Hi from Greetings client");

        GreetingClient client = new GreetingClient();
        client.run();
    }

    private void run(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",
                50051)
                .usePlaintext()//to disable default
                .build();

        //doUnaryCall(channel);
        //doServerStreamingCall(channel);
        //doClientStreamingCall(channel);
        doBiDirectionalStreamingCall(channel);


        //shutdown
        System.out.println("shutting down chaneel");
        channel.shutdown();


    }

    private void doClientStreamingCall(ManagedChannel channel){
        CountDownLatch latch = new CountDownLatch(1);
       //needs to be a sync stub to send client streaming
        GreetServiceGrpc.GreetServiceStub asycClient = GreetServiceGrpc.newStub(channel);

        StreamObserver<LongGreetRequest> requestStreamObserver = asycClient.longGreet(new StreamObserver<LongGreetResponse>() {
            @Override
            public void onNext(LongGreetResponse value) {
                //we get response from server
                System.out.println("received reponse from server:"+value.getResponse());
            }

            @Override
            public void onError(Throwable t) {
              //error from server
            }

            @Override
            public void onCompleted() {
                //server is done sending response
                System.out.println("server has completed sending response from server");
                latch.countDown();
            }
        });

         System.out.println("Client is sending message 1");
        requestStreamObserver.onNext(LongGreetRequest.newBuilder()
                   .setGreeting(Greeting.newBuilder().setFirstName("Gary moore").build()).build());

        System.out.println("Client is sending message 2");

        requestStreamObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("Peter Green").build()).build());
        System.out.println("Client is sending message 3");


        requestStreamObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("Jeff Beck").build()).build());

        requestStreamObserver.onCompleted();//client is done sending data
        try {
            latch.await(3 , TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doServerStreamingCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceBlockingStub synGreetClient =
                GreetServiceGrpc.newBlockingStub(channel);

        //server streaming api call in blocking call
        GreetManyTimesRequest greetManyTimesRequest =
                GreetManyTimesRequest.newBuilder().setGreeting(Greeting.newBuilder().setFirstName("Gallahar").build()).build();
        synGreetClient.greetManyTimes(greetManyTimesRequest).forEachRemaining(greetManyTimesResponse -> {
            System.out.println(greetManyTimesResponse.getResult());
        });

    }

    private void doUnaryCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceBlockingStub synGreetClient =
                GreetServiceGrpc.newBlockingStub(channel);

        Greeting greeting = Greeting.newBuilder()
                .setFirstName("Clapton")
                .setLastName("Eric").build();
        GreetRequest greetRequest = GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        //unary API call

         GreetResponse greetResponse = synGreetClient.greet(greetRequest);
         System.out.println(greetResponse.getResult());

    }

    private void doBiDirectionalStreamingCall(ManagedChannel channel) {
        CountDownLatch latch = new CountDownLatch(1);
        GreetServiceGrpc.GreetServiceStub asynClient = GreetServiceGrpc.newStub(channel);

        StreamObserver<GreetEveryoneRequest> requestStreamObserver = asynClient.greetEveryone(new StreamObserver<GreetEveryoneResponse>() {
            @Override
            public void onNext(GreetEveryoneResponse value) {
               System.out.println("Response from server:"+value.getResult());
            }

            @Override
            public void onError(Throwable t) {
               latch.countDown();
            }

            @Override
            public void onCompleted() {
                    System.out.println("server is done sending data");
                    latch.countDown();
            }
        });

        List<String> names = Arrays.asList("Peter Green", "Jeff Beck", "Hopkins");
        names.forEach(name -> {
            System.out.println("sending--"+name);
            requestStreamObserver.onNext(GreetEveryoneRequest.newBuilder()
                    .setGreeting(Greeting.newBuilder().setFirstName(name)).build());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        requestStreamObserver.onCompleted();

        try {
            latch.await(3,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
