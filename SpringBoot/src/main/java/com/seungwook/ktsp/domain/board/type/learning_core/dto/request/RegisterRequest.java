package com.seungwook.ktsp.domain.board.type.learning_core.dto.request;

import com.seungwook.ktsp.domain.board.common.entity.enums.Campus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RegisterRequest {
    
    @Schema(description = "캠퍼스")
    @NotNull
    private final Campus campus;

    @Schema(description = "최대 팀원 수")
    @NotNull(message = "팀원 수는 필수값입니다.")
    private Integer teamSize;
    
    @Schema(description = "게시글 제목")
    @NotBlank
    @Size(min = 2, max = 50, message = "게시글 제목은 최소 2자 ~ 최대 50자 입니다.")
    private final String title;

    @Schema(description = "게시글 내용")
    @NotBlank
    @Size(min = 2, max = 10000, message = "게시글 내용은 최소 2자 이상, 최대 10000자 이하입니다.")
    private final String content;

    @Schema(description = "첨부파일 UUID", example = "550e8400-e29b-41d4-a716-446655440000")
    @Size(max = 5, message = "첨부파일은 최대 5개 까지만 업로드 가능합니다.")
    private final List<String> attachedFiles;
}
