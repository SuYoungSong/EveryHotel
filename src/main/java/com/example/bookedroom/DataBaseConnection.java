package com.example.bookedroom;

import java.sql.*;

public class DataBaseConnection {
    Connection connection = null;
    Statement statment = null;
    ResultSet resultSet = null;

    public DataBaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "githun가림");
            statment = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DB연결 오류 :" + e.getMessage());
        }
    }

    public ResultSet sendQuryGet(String SQL){
        try {
            System.out.println(SQL);
            resultSet = statment.executeQuery(SQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            resultSet = null;
        } catch (Exception e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public int sendQuryPost(String SQL){
        try {
            System.out.println(SQL);
            statment.execute(SQL);
        } catch(SQLException e) {
            System.out.println(e.getMessage());
            return -1;              // 잘못된 sql문 입력
        } catch (Exception e) {
            e.printStackTrace();    // 나머지 에러
        }
        return 0;                   // 정상 처리
    }
}


