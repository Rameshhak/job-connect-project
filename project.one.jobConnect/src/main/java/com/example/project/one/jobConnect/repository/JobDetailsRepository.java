package com.example.project.one.jobConnect.repository;

import com.example.project.one.jobConnect.document.JobDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.regex.Pattern;

public interface JobDetailsRepository extends MongoRepository<JobDetails,Integer> {

    List<JobDetails> findByLocationOrTitle(String location, String title);

    List<JobDetails> findByTitleRegexOrDescriptionRegex(Pattern title, Pattern location);



    @Query("{ '$or': [ "
            + "  { 'title': { $regex: ?0, $options: 'i' } }, "
            + "  { 'description': { $regex: ?0, $options: 'i' } }, "
            + "  { 'location': { $regex: ?0, $options: 'i' } } "
            + "] }")
    List<JobDetails> searchByKeyword(String keyword);
}
