
package Clases;

import java.sql.Connection;
import java.sql.DriverManager;


public class Conexion {
     public static final String url = "jdbc:mysql://localhost:3306/scr_duarte?useLegacyDatetimeCode=false&serverTimezone=America/Lima";
    public static final String user = "root";
    public static final String password = "1234";
    
        public static Connection getConection(){
        Connection conn = null;
        try{

                conn = (Connection) DriverManager.getConnection(url, user, password);
               
        }catch(Exception e){
            System.out.println(e);
        }
        return conn;
    }
}
