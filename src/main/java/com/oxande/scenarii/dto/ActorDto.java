package com.oxande.scenarii.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActorDto {
    private String age;
    private String name;
    private String comments;
    private char sex;
}
