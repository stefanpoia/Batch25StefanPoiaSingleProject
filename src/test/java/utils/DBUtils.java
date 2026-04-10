package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DBUtils {

    public static List<Map<String,String>> fetch(String query){


        String dburl=ConfigReader.read("dbURL");
        String userName=ConfigReader.read("dbUserName");
        String password=ConfigReader.read("dbPassword");

        List<Map<String,String>> mapList=new ArrayList<>();

        try( Connection connection= DriverManager.getConnection(dburl,userName,password);

             Statement statement=connection.createStatement()) {
            ResultSet resultSet=statement.executeQuery(query);
            ResultSetMetaData rsmd=resultSet.getMetaData();
            while (resultSet.next()){

                Map<String,String> rowMap=new LinkedHashMap<>();
                for (int i = 1; i <=rsmd.getColumnCount() ; i++) {
                    String key= rsmd.getColumnLabel(i);
                    String value=resultSet.getString(i);
                    rowMap.put(key,value);
                }

                mapList.add(rowMap);

            }

        }catch (Exception e){
            e.printStackTrace();
        }



        return mapList;


    }

}
