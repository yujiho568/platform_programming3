import edu.pnu.myjdbc.spec.Connection;

import edu.pnu.myjdbc.spec.DriverManager;

import edu.pnu.myjdbc.spec.ResultSet;

import edu.pnu.myjdbc.spec.Statement;

//interface import

public class MyJdbcMysqlTest {

    public static void main(String[] args) {

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "username", "password");

        Statement stmt = conn.createStatement();

        ResultSet resultSet = stmt.executeQuery("SELECT * FROM users");

        while(resultSet.next()) {

            System.out.print("ID: " + resultSet.getInt("id"));

            System.out.print(", Age: " + resultSet.getInt("age"));

            System.out.print(", First: " + resultSet.getString("first"));

            System.out.println(", Last: " + resultSet.getString("last"));

        }

    }

}