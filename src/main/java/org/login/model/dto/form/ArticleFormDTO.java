package org.login.model.dto.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ArticleFormDTO {

    @NotEmpty(message = "タイトルを入力してください。")
    @Size(max = 255, message = "タイトルは255文字以内で入力してください。")
    private String title;

    @NotEmpty(message = "本文を入力してください")
    private String content;

}
