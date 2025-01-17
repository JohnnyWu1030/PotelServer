package potel.petsfile.dao;

import java.io.InputStream;
import java.util.List;

import potel.petsfile.vo.Cat;
import potel.petsfile.vo.Dog;

public interface PetsFileDao {
    List<Dog> selectDog();
    
    List<Cat> selectCat();
    
    Integer insertImage(InputStream imageStream);
    
	void addPost(int memberId, String title, String content, int imageId);

    boolean addDog(String dogOwner, String dogName, String dogBreed, String dogGender, int dogImages);
    
    boolean addCat(String catOwner, String catName, String catBreed, String catGender, int catImages);

    boolean deleteDog(int dogId);

    boolean deleteCat(int catId);
    
	void updatPostWithImage(int postId, String title, String content, int imageId);

	void updatePostWithoutImage(int postId, String title, String content);

    void updateDog(int dogId, String dogOwner, String dogName, String dogBreed, String dogGender, int dogImages);
    
    void updateCat(int catId, String catOwner, String catName, String catBreed, String catGender, int catImages);
}

