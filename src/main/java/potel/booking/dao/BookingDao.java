package potel.booking.dao;

import java.util.List;
import potel.booking.vo.Order;


import potel.booking.vo.RoomType;

public interface BookingDao {

	List<RoomType> findAllRoomType();
	
	List<RoomType> findRoomTypeById(int roomTypeId);
	
	byte[] findImageDataById(int imageId); // 新增方法以查詢圖片數據
	
	int addOrder(Order order);

}
