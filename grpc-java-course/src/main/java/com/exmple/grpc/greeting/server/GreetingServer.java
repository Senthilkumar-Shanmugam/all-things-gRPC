package com.exmple.grpc.greeting.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.File;
import java.io.IOException;

public class GreetingServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Hello GRPC");

        //plaintext server
        /*Server server = ServerBuilder.forPort(50051)
                                     .addService(new GreetServiceImpl())
                                     .build();*/

        //secure server
        Server server = ServerBuilder.forPort(50051)
                                     .addService(new GreetServiceImpl())
                                     .useTransportSecurity(new File("ssl/server.crt"),
                                                           new File("ssl/server.pem")).build();
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            System.out.println("received shutdown request");
            server.shutdown();
            System.out.println("shutdown successfully");
        }));
        server.awaitTermination();
    }
}
