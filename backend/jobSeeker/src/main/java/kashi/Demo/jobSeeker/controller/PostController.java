package kashi.Demo.jobSeeker.controller;

import kashi.Demo.jobSeeker.entity.Job;
import kashi.Demo.jobSeeker.repository.JobRepository;
import kashi.Demo.jobSeeker.repository.SearchText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {
    @Autowired
    SearchText srepo;

    @Autowired
    JobRepository repo;

    @GetMapping("/job")
    public List<Job> getAllJob() {
        return repo.findAll();

    }
    @GetMapping("/job/{text}")
    public List<Job> searchText(@PathVariable String text){
        return srepo.searchByText(text);

    }


    @PostMapping("/job")
    public void addJob(@RequestBody Job job) {
        repo.save(job);
    }
}