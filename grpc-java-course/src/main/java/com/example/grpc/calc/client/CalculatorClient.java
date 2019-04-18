package com.example.grpc.calc.client;

import com.proto.calculator.CalculatorGrpc;
import com.proto.calculator.SumRequest;
import com.proto.calculator.SumResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",
                                                                     50052).usePlaintext().build();
        CalculatorGrpc.CalculatorBlockingStub synClient = CalculatorGrpc.newBlockingStub(channel);

        SumRequest request = SumRequest.newBuilder()
                                       .setFirstNumber(10)
                                        .setSecondNumber(15).build();

        SumResponse response = synClient.sum(request);

        System.out.println(response.getSumResult());

        channel.shutdown();

    }
}
