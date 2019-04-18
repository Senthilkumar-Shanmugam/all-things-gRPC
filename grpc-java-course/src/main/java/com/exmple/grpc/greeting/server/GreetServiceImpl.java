package com.exmple.grpc.greeting.server;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc.GreetServiceImplBase;
import com.proto.greet.Greeting;
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
}
