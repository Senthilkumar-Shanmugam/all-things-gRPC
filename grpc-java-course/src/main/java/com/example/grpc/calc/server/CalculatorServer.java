package com.example.grpc.calc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import jdk.internal.org.objectweb.asm.commons.SerialVersionUIDAdder;

import java.io.IOException;

public class CalculatorServer {
    public static void main(String[] args) throws InterruptedException, IOException {
        Server server = ServerBuilder.forPort(50052)
                                      .addService(new CalculatorServiceImpl())
                                      .addService(ProtoReflectionService.newInstance())//reflection
                                      .build();

        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            System.out.println("received shutdown request");
            server.shutdown();
            System.out.println("shutdown successfully");
        }));
        server.awaitTermination();
    }
}
