package src;

import src.utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

public interface Helper {
    Connection connection = ConnectionUtil.getConnectionUtil().getConnection();
    
    ResultSet getDataById(String id);
    ResultSet getAllData();
    void deleteDataById(String id);
    void deleteAllData();
    void insertData(String id);
    void insertData(String id, HashMap<String, String> options);
    void updateData(String id, HashMap<String, String> options);
}
