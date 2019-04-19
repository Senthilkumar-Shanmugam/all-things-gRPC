package com.example.grpc.calc.server;

import com.proto.calculator.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl  extends CalculatorGrpc.CalculatorImplBase {
    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {

        int sum = request.getFirstNumber() + request.getSecondNumber();

        SumResponse response = SumResponse.newBuilder()
                                          .setSumResult(sum).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void primeNumberDecomposition(PrimeNumberDecompositionRequest request, StreamObserver<PrimeNumberDecompositionResponse> responseObserver) {
        Integer number = request.getNumber();
        Integer devisor = 2;

        while(number > 1){
                if(number % devisor == 0) {
                   number = number /devisor;
                   responseObserver.onNext(PrimeNumberDecompositionResponse.newBuilder()
                                            .setPrimeFactor(devisor)
                                            .build());
                }else{
                    devisor++;
                }
         }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<ComputeAverageRequest> computeAverage(StreamObserver<ComputeAverageResponse> responseObserver) {
        StreamObserver<ComputeAverageRequest> streamObserverRequest = new StreamObserver<ComputeAverageRequest>() {
            int sum = 0;
            int count = 0;
            @Override
            public void onNext(ComputeAverageRequest value) {
               sum += value.getNumber();
               count++;
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
              double average =  (double)sum/count;
              responseObserver.onNext(ComputeAverageResponse.newBuilder().setAverage(average).build());
              responseObserver.onCompleted();
            }
        };
        return streamObserverRequest;
    }

    @Override
    public void squareRoot(SquareRootRequest request, StreamObserver<SquareRootResponse> responseObserver) {
        Integer number = request.getNumber();

        if(number > 0){
            double sqrRoot = Math.sqrt(number);
            responseObserver.onNext(SquareRootResponse.newBuilder()
                                                      .setSrqRoot(sqrRoot)
                                                      .build());
            responseObserver.onCompleted();
        }else{
            responseObserver.onError(Status.INVALID_ARGUMENT
                                            .withDescription("The number passed is negative")
                                            .asRuntimeException());
        }
    }
}
