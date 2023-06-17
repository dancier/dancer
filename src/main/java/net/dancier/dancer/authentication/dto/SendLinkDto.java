package net.dancier.dancer.authentication.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class SendLinkDto {

    @NotBlank
    @Size(max = 100)
    @Email
    private String email;

}
