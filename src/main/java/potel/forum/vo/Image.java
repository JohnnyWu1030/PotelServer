package potel.forum.vo;

import lombok.Data;

@Data
public class Image {
    private int imageId;
    private byte[] imageData;
    private String createDate;
    private String modifyDate;
}
