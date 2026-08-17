package kashi.Demo.jobSeeker.repository;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import kashi.Demo.jobSeeker.entity.Job;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SearchRepository implements SearchText {
    @Autowired
    MongoClient client;
    @Autowired
    MongoConverter converter;

    @Override
    public List<Job> searchByText(String text) {


        final List<Job> jai = new ArrayList<>();
        MongoDatabase dataBase = client.getDatabase("cluster0");
        MongoCollection<Document> collection = dataBase.getCollection("jobseeker");
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search",
                        new Document("text",
                        new Document("query", text)
                        .append("path", Arrays.asList("techs", "desc", "profile")))),
                        new Document("$sort",
                        new Document("exp", 1L)),
                        new Document("$limit", 5L)));

        result.forEach(doc ->jai.add(converter.read(Job.class,doc)));

        return jai;
    }
}
