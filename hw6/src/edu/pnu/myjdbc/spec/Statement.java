package edu.pnu.myjdbc.spec;

public interface Statement {
    ResultSet executeQuery(String query);
}