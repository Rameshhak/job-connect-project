package com.example.project.one.jobConnect.document;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("keywords")
@Data
public class Keyword {

    private String value;

    public String getValue(){
       this.value = value;
       return value;
    }
}
