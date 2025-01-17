package potel.petsfile.vo;

import lombok.Data;

@Data
public class Cat {
    private String catOwner;
    private int catId;
    private String catName;
    private String catBreed;
    private String catGender;
    private int catImages;
}
