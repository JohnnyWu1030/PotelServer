package potel.petsfile.service.impl;

import java.io.InputStream;
import java.util.List;

import potel.petsfile.dao.PetsFileDao;
import potel.petsfile.dao.impl.PetsFileDaoImpl;
import potel.petsfile.service.PetsFileService;
import potel.petsfile.vo.Cat;
import potel.petsfile.vo.Dog;

public class PetsFileServiceImpl implements PetsFileService {

	private PetsFileDao petsFileDao;

	public PetsFileServiceImpl() {
		this.petsFileDao = new PetsFileDaoImpl();
	}

	@Override
    public List<Dog> getDog() {
        System.out.println("PetsFileServiceImpl: Retrieving  dogs");
        List<Dog> dogs = petsFileDao.selectDog();
        System.out.println("Retrieved dogs: " + (dogs != null ? dogs.size() : 0)); // 打印返回的狗的數量
        return dogs;
    }

    @Override
    public List<Cat> getCat() {
        System.out.println("PetsFileServiceImpl: Retrieving  cats");
        List<Cat> cats = petsFileDao.selectCat();
        System.out.println("Retrieved cats: " + (cats != null ? cats.size() : 0)); // 打印返回的貓的數量
        return cats;
    }

    @Override
	public int saveImageToDatabase(InputStream imageStream) {
		return petsFileDao.insertImage(imageStream);
	}

	@Override
	public void addPost(int memberId, String title, String content, int imageId) {
		petsFileDao.addPost(memberId, title, content, imageId);
	}
    @Override
    public boolean addDog(String dogOwner, String dogName, String dogBreed, String dogGender, int dogImages) {
        System.out.println("PetsFileServiceImpl: Adding new dog");
        
        // 调用 DAO 层的方法来添加狗
        boolean isAdded = petsFileDao.addDog(dogOwner, dogName, dogBreed, dogGender, dogImages);
        
        // 返回操作是否成功
        return isAdded;
    }

    @Override
    public boolean addCat(String catOwner, String catName, String catBreed, String catGender, int catImages) {
        System.out.println("PetsFileServiceImpl: Adding new cat");

        // 调用 DAO 层的方法来添加猫
        boolean isAdded = petsFileDao.addCat(catOwner, catName, catBreed, catGender, catImages);
        
        // 返回操作是否成功
        return isAdded;
    }

    @Override
    public boolean deleteDog(int dogId) {
        System.out.println("PetsFileServiceImpl: Deleting dog with ID: " + dogId);
        return petsFileDao.deleteDog(dogId);
    }

    @Override
    public boolean deleteCat(int catId) {
        System.out.println("PetsFileServiceImpl: Deleting cat with ID: " + catId);
        return petsFileDao.deleteCat(catId);
    }

    @Override
	public void updatPostWithImage(int postId, String title, String content, Integer imageId) {
    	petsFileDao.updatPostWithImage(postId, title, content, imageId);
	}

	@Override
	public void updatPostWithoutImage(int postId, String title, String content) {
		petsFileDao.updatePostWithoutImage(postId, title, content);
		
	}
    @Override
    public void updateDog(int dogId, String dogOwner, String dogName, String dogBreed, String dogGender, int dogImages) {
        System.out.println("PetsFileServiceImpl: Updating dog with ID: " + dogId);
        petsFileDao.updateDog(dogId, dogOwner, dogName, dogBreed, dogGender, dogImages);
    }

    @Override
    public void updateCat(int catId, String catOwner, String catName, String catBreed, String catGender, int catImages) {
        System.out.println("PetsFileServiceImpl: Updating cat with ID: " + catId);
        petsFileDao.updateCat(catId, catOwner, catName, catBreed, catGender, catImages);
    }
}