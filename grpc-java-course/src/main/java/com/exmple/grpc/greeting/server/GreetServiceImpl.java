package com.exmple.grpc.greeting.server;

import com.proto.greet.*;
import com.proto.greet.GreetServiceGrpc.GreetServiceImplBase;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceImplBase {
    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {

        Greeting greeting = request.getGreeting();
        String fname = ((Greeting) greeting).getFirstName();

        String result = "Hello" + fname;

        GreetResponse response = GreetResponse.newBuilder()
                                              .setResult(result)
                                              .build();

        //send repsonse
        responseObserver.onNext(response);

        //complete rpc call
        responseObserver.onCompleted();
        //super.greet(request, responseObserver);
    }

    @Override
    public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {
        String firstName = request.getGreeting().getFirstName();

        try {
            for (int i = 0; i < 10; i++) {
                String result = "Hello " + firstName + ", response number: " + i;
                GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder()
                        .setResult(result)
                        .build();

                responseObserver.onNext(response);
                Thread.sleep(1000L);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {
        StreamObserver<LongGreetRequest>  streamRequestObserver = new StreamObserver<LongGreetRequest>() {
            String result = "";
            @Override
            public void onNext(LongGreetRequest value) {
                //client message arrive
                System.out.println("Client message arrived");
                result += "Hello " + value.getGreeting().getFirstName()+ "!";
            }

            @Override
            public void onError(Throwable t) {
              //client error
            }

            @Override
            public void onCompleted() {
              //client is done sending. we could send response here
                System.out.println("client is done sending messages");
                responseObserver.onNext(LongGreetResponse.newBuilder().setResponse(result).build());
                responseObserver.onCompleted();
            }
        };


        return streamRequestObserver;
    }

    @Override
    public StreamObserver<GreetEveryoneRequest> greetEveryone(StreamObserver<GreetEveryoneResponse> responseObserver) {
        StreamObserver<GreetEveryoneRequest>  requestStreamObserver = new StreamObserver<GreetEveryoneRequest>() {
            @Override
            public void onNext(GreetEveryoneRequest value) {
                 String message = "Hello "+ value.getGreeting() +"!";
                GreetEveryoneResponse response = GreetEveryoneResponse.newBuilder().setResult(message).build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable t) {
              //nothing
            }

            @Override
            public void onCompleted() {
               responseObserver.onCompleted();
            }
        };
        return requestStreamObserver;
    }

    @Override
    public void greetWithDeadline(GreetWithDeadlineRequest request, StreamObserver<GreetWithDeadlineResponse> responseObserver) {
        System.out.println("inside greetWithDeadline ");
        Context current = Context.current();
        try {
            for (int i = 0; i < 3; i++) {
                if(!current.isCancelled()) {
                    System.out.println("sleeping for 100 milliseconds");
                    Thread.sleep(100);
                }else{
                    return;
                }
            }
            responseObserver.onNext(GreetWithDeadlineResponse.newBuilder()
                    .setResponse("Hello " + request.getGreeting().getFirstName() + "!")
                    .build());

            responseObserver.onCompleted();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}

