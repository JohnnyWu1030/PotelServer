package potel.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

public class ReadImage {

	public static void main(String[] args) {
		try {
			File dir = new File("C:\\27-JohnnyWu\\Work\\Project\\pics\\PotelBooking\\");
			File[] files = dir.listFiles();
			DataSource ds = JDBCConstants.getDataSource2();
			try (Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("insert into images(imagedata,imageid) values(?,?)");) {

				for (File file : files) {
					System.out.println("file=" + file.getName());
					String imgid = file.getName().substring(0, file.getName().lastIndexOf('.'));
					System.out.println("imageid=" + imgid);

					Path path = Paths.get(file.getPath());
					byte[] imgdata = Files.readAllBytes(path);

					pstmt.clearParameters();
					pstmt.setBytes(1, imgdata);
					pstmt.setInt(2, Integer.parseInt(imgid));
					int effect = pstmt.executeUpdate();
					System.out.println("take effect=" + effect);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
