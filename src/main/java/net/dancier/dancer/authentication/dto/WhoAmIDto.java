package net.dancier.dancer.authentication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WhoAmIDto {

    private Boolean emailVerified;

    private List<String> roles;

    private String emailAddress;

    public static class WhoAmIDtoBuilder {
        private List<String> roles = new ArrayList<>();
        private Boolean emailVerified;
        private String emailAddress;

        public WhoAmIDtoBuilder() {}

        public WhoAmIDtoBuilder addRole(String role) {
            this.roles.add(role);
            return this;
        }

        public WhoAmIDtoBuilder isEmailVerified(Boolean isVerified) {
            this.emailVerified = isVerified;
            return this;
        }

        public WhoAmIDtoBuilder withEmailAddress(String address) {
            this.emailAddress = address;
            return this;
        }

        public WhoAmIDto build() {
            WhoAmIDto whoAmIDto = new WhoAmIDto();
            whoAmIDto.roles = this.roles;
            whoAmIDto.emailVerified = this.emailVerified;
            whoAmIDto.emailAddress = this.emailAddress;
            return whoAmIDto;
        }
    }

}
