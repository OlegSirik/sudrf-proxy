package com.vsk.courtrf;


import com.vsk.courtrf.lawcase.entity.LawCase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
//import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DBUtil {
    private static Connection sqlConnection;
    private static String db_url = "";
    private static String db_user = "";
    private static String db_pswd = "";

    public static void connectDB(String pURL, String pLogin, String pPswd) {
        db_url = pURL;
        db_user = pLogin;
        db_pswd = pPswd;

        connPostgre();
    }

    public static void connOra() {
        try {
            Class.forName ("oracle.jdbc.OracleDriver");
            //sqlConnection = DriverManager.getConnection("jdbc:oracle:thin:@//172.16.48.14:1521/gdii", "delivery_1c", "re29U6un");

            sqlConnection = DriverManager.getConnection("jdbc:oracle:thin:@" + db_url, db_user, db_pswd );

            System.out.println("SQL CONNECTED :: " + db_url );
        } catch (Exception e) {
            System.out.println("ERROR::" + e);
        }
    }

    public static void connPostgre() {
        try {
            Class.forName ("org.postgresql.Driver");
            //sqlConnection = DriverManager.getConnection("jdbc:oracle:thin:@//172.16.48.14:1521/gdii", "delivery_1c", "re29U6un");

            sqlConnection = DriverManager.getConnection(db_url, db_user, db_pswd );

            System.out.println("SQL CONNECTED :: " + db_url );
        } catch (Exception e) {
            System.out.println("ERROR::" + e);
        }
    }

    public static void connectDB() {
    }

    public void saveLawCase(LawCase pDT) {

    }
}
