package net.dancier.dancer.authentication.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
public class SendLinkDto {

    @NotBlank
    @Size(max = 100)
    @Email
    private String email;

}
