package com.example.facades;

import com.example.models.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.lambdaworks.redis.*;

import java.util.ArrayList;
import java.util.List;

public class RedisFacade {
    GsonBuilder GSON = new GsonBuilder();


    public void connectTest() {
            RedisClient redisClient = new RedisClient(
                    RedisURI.create("redis://127.0.0.1:6379"));
            RedisConnection<String, String> connection = redisClient.connect();

            System.out.println("Connected to Redis");

            connection.close();
            redisClient.shutdown();
        }

        public List<Post> getAllPosts() throws JsonProcessingException {
            RedisClient redisClient = new RedisClient(
                    RedisURI.create("redis://127.0.0.1:6379"));
            RedisConnection<String, String> connection = redisClient.connect();
            List<String> list = connection.lrange("allPosts", 0, -1);
            List<Post> posts = new ArrayList<>();
            if(!list.isEmpty()){
                for (String s: list
                     ) {
                    posts.add(GSON.create().fromJson(s, Post.class));
                }
            }
            return posts;
        }

        public void addCache(List<Post> postsDB) {
            RedisClient redisClient = new RedisClient(
                    RedisURI.create("redis://127.0.0.1:6379"));
            RedisConnection<String, String> connection = redisClient.connect();

            connection.flushall();

            for (Post p : postsDB) {
                connection.lpush("allPosts", GSON.create().toJson(p));
            }
            connection.expire("allPosts", 3600);
            connection.close();
            redisClient.shutdown();
            }

}


