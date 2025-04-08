package com.example.forum.controller.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentForm {

    private int id;
    @NotBlank(message = "コメントを入力してください")
    private String text;
    private Integer reportId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
