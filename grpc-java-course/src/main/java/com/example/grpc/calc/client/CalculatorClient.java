package com.example.grpc.calc.client;

import com.proto.calculator.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",
                                                                     50052).usePlaintext().build();
        CalculatorGrpc.CalculatorBlockingStub synClient = CalculatorGrpc.newBlockingStub(channel);

        SumRequest request = SumRequest.newBuilder()
                                       .setFirstNumber(10)
                                        .setSecondNumber(15).build();

        //SumResponse response = synClient.sum(request);
        //System.out.println(response.getSumResult());

        //Streaming api call
        /*Integer number = 578758876;

        synClient.primeNumberDecomposition(PrimeNumberDecompositionRequest.newBuilder()
                                        .setNumber(number).build())
                                        .forEachRemaining(primeNumberDecompositionResponse -> {
                                            System.out.println(primeNumberDecompositionResponse.getPrimeFactor());
                                        });*/

/*
        CountDownLatch latch = new CountDownLatch(1);
        CalculatorGrpc.CalculatorStub asynClient = CalculatorGrpc.newStub(channel);
        StreamObserver<ComputeAverageRequest> streamRequestObserver = asynClient.computeAverage(new StreamObserver<ComputeAverageResponse>() {
            @Override
            public void onNext(ComputeAverageResponse value) {
                System.out.println("Average response from server:" + value.getAverage());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                System.out.println("server is done sending response");
                latch.countDown();
            }
        });

        streamRequestObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(10).build());
        streamRequestObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(100).build());
        streamRequestObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(50).build());
        streamRequestObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(10).build());
        streamRequestObserver.onCompleted();

        try {
            latch.await(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/

         //square root error call
        int number = -10;
        try {
            synClient.squareRoot(SquareRootRequest.newBuilder().setNumber(number).build());
        }catch(StatusRuntimeException e){
            System.out.println("Got an exception for square root:");
            e.printStackTrace();
        }
       channel.shutdown();

    }
}
