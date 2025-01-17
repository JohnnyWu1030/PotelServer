package potel.petsfile.vo;

import lombok.Data;

@Data
public class Dog {
    private String dogOwner;
    private int dogId;
    private String dogName;
    private String dogBreed;
    private String dogGender;
    private int dogImages;
}