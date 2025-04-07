package com.example.forum.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentForm {

    private int id;
    private String text;
    private Integer reportId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
