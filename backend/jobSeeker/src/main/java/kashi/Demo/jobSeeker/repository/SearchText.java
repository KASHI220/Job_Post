package kashi.Demo.jobSeeker.repository;

import kashi.Demo.jobSeeker.entity.Job;

import java.util.List;

public interface SearchText {
    public List<Job> searchByText(String text);
}
