package com.example.grpc.blog.client;

import com.proto.blog.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BlogClient {
    public static void main(String[] args) {
        BlogClient client = new BlogClient();
        client.run();
    }

    private void run(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",50053)
                                                      .usePlaintext()
                                                      .build();

        BlogServiceGrpc.BlogServiceBlockingStub stub = BlogServiceGrpc.newBlockingStub(channel);
        Blog blog = Blog.newBuilder()
                        .setAuthorId("santana")
                        .setTitle("secthirdond Blog")
                        .setContent("This is my third blog")
                        .build();

        CreateBlogResponse blogResponse = stub.createBlog(CreateBlogRequest.newBuilder().setBlog(blog).build());

        String blogId = blogResponse.getBlog().getId();
        System.out.println("Id of the newly created blog:"+blogId);


        //read blog

        ReadBlogResponse readBlogResponse = stub.readBlog(ReadBlogRequest.newBuilder()
                                    .setBlogId(blogId).build());

        System.out.println("blog read>>"+blogResponse.getBlog().toString());


        // we list the blogs in our database
        stub.listBlog(ListBlogRequest.newBuilder().build()).forEachRemaining(
                listBlogResponse -> System.out.println(listBlogResponse.getBlog().toString())
        );
    }
}
