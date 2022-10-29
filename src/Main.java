import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        File f = new File("MySQL.png");
        File f2 = new File("Copy_MySQL.png");
        byte [] myImage;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaworld",
                "javarafail", "javauser1234")){
            try (
                 BufferedInputStream bs = new BufferedInputStream(new FileInputStream(f));
                 BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f2))){
                myImage = bs.readAllBytes();
                String query = "INSERT INTO sqlimage (image)\n" +
                        "VALUES (?)";
                PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                stmt.setBlob(1, new SerialBlob(myImage));
                stmt.executeUpdate();
                ResultSet result = stmt.getGeneratedKeys();
                result.next();
                int id = result.getInt(1);
                query = "SELECT image FROM sqlimage WHERE imageID = ?";
                stmt = conn.prepareStatement(query);
                stmt.setInt(1,id);
                ResultSet newResult = stmt.executeQuery();
                newResult.next();
                bos.write(newResult.getBytes(1));

            } catch (IOException b) {
                System.err.println(b);
            }





        } catch (SQLException s) {
            System.err.println(s);
        }

    }
}