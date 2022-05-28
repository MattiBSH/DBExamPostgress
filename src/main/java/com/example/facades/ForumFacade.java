package com.example.facades;

import com.example.models.Comment;
import com.example.models.Post;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Filters.eq;

public class ForumFacade {
    private MongoClient mongoClient;
    private CodecProvider pojoCodecProvider;
    private CodecRegistry pojoCodecRegistry;

    public ForumFacade() throws UnknownHostException {
        this.mongoClient = initMongoClient();
        initCodec();
    }

    public MongoClient initMongoClient() {
        if (mongoClient == null){
            mongoClient = MongoClients.create("mongodb://localhost:27017");
        }
        return mongoClient;
    }
    public void initCodec() {
        if (pojoCodecProvider == null){
            pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        }
        if (pojoCodecRegistry == null){
            pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        }
    }

    public List<Post> getAllPosts(){
        RedisFacade redisFacade = new RedisFacade();
        List<Post> posts = new ArrayList();
        MongoDatabase database = mongoClient.getDatabase("Forum").withCodecRegistry(pojoCodecRegistry);
        MongoCollection<Post> postsCol = database.getCollection("Posts", Post.class);
        postsCol.find().into(posts).forEach(post -> System.out.println(post.toString()));
        for (Post p: posts
             ) {
            p.setId_String(p.getId().toHexString());
        }
        redisFacade.addCache(posts);
        return posts;
    }

    public Post addPost(Post post){
        MongoDatabase database = mongoClient.getDatabase("Forum").withCodecRegistry(pojoCodecRegistry);
        MongoCollection<Post> postsCol = database.getCollection("Posts", Post.class);

        postsCol.insertOne(post);
        RedisFacade redisFacade = new RedisFacade();
        redisFacade.addPost(post);
        return post;
    }

    public Post likePost(Post post){
        MongoDatabase database = mongoClient.getDatabase("Forum").withCodecRegistry(pojoCodecRegistry);
        MongoCollection<Post> postsCol = database.getCollection("Posts", Post.class);

        Post p = postsCol.find(eq("_id", post.getId())).first();
        System.out.println(p.toString());
        Bson queryFilter = new Document("_id", p.getId());
        p.likePost();

        FindOneAndReplaceOptions returnDocAfterReplace = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
        Post updatedPost = postsCol.findOneAndReplace(queryFilter, p, returnDocAfterReplace);
        System.out.println(updatedPost.toString());

        return updatedPost;
    }

    public Post commentPost(Comment comment){
        MongoDatabase database = mongoClient.getDatabase("Forum").withCodecRegistry(pojoCodecRegistry);
        MongoCollection<Post> postsCol = database.getCollection("Posts", Post.class);

        Post p = postsCol.find(eq("_id", comment.getId())).first();
        Bson queryFilter = new Document("_id", p.getId());
        p.addComment(comment.getContent());

        FindOneAndReplaceOptions returnDocAfterReplace = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
        Post updatedPost = postsCol.findOneAndReplace(queryFilter, p, returnDocAfterReplace);
        System.out.println(updatedPost.toString());

        return updatedPost;
    }

}
