package com.example.models;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.List;

public class Post {

    @BsonProperty("_id")
    @BsonId
    private ObjectId id;
    private String id_String;
    private String author;
    private String content;
    private List<String> comments;
    private int likes;

    public Post(){
    }

    public Post(String author, String content, List<String> comments, int likes) {
        this.author = author;
        this.content = content;
        this.comments = comments;
        this.likes = likes;
    }

    public Post(ObjectId id, String author, String content, List<String> comments, int likes) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.comments = comments;
        this.likes = likes;
    }

    public Post(ObjectId id, String id_String, String author, String content, List<String> comments, int likes) {
        this.id = id;
        this.id_String = id_String;
        this.author = author;
        this.content = content;
        this.comments = comments;
        this.likes = likes;
    }

    public void likePost(){
        this.likes++;
    }

    public String getId_String() {
        return id_String;
    }

    public void setId_String(String id_String) {
        this.id_String = id_String;
    }

    public void addComment(String comment){
        this.comments.add(comment);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", comments=" + comments +
                ", likes=" + likes +
                '}';
    }
}
