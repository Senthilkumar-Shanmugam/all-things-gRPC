package com.example.grpc.calc.server;

import com.proto.calculator.CalculatorGrpc;
import com.proto.calculator.SumRequest;
import com.proto.calculator.SumResponse;
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
}
