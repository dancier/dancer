package net.dancier.resources.login;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OidcProvider {

    private int order;
    private String name;
    private String description;
    private String icon_link;
    private String link;

}
