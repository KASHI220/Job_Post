package kashi.Demo.jobSeeker.repository;

import kashi.Demo.jobSeeker.entity.Job;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRepository  extends MongoRepository<Job, String> {

}
