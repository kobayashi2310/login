package org.login.model.dto.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentFormDTO {

    @NotBlank(message = "コメント内容は必須です")
    @Size(max = 500, message = "コメントは500文字以内で入力してください")
    private String content;

}
