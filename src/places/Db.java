package places;

import places.util.ReadFile;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author motan
 * @date 7/5/14
 */
public class Db {



    public static void startDb() {

        String dbURL = "jdbc:derby:mediTrackDb/db;create=true;user=motan;password=swag";

        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
            Connection conn = DriverManager.getConnection(dbURL);

            createPhTable(conn);

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

        public static void createPhTable (Connection conn) throws NullPointerException {

            String sql = null;

            try {
                sql = ReadFile.read("/home/motan/tools/gson_server/src/places/createPhTable.sql", StandardCharsets.UTF_8);

                Statement st = conn.createStatement();
                st.executeUpdate(sql);

                System.out.println("Created table!");

                st.close();
            }
             catch (Exception e) {
                e.printStackTrace();
            }


    }
}
