package com.exmple.grpc.greeting.server;

import com.proto.greet.*;
import com.proto.greet.GreetServiceGrpc.GreetServiceImplBase;
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
    }

