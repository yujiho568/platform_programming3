package edu.pnu.myjdbc.spec;

public interface ResultSet {
    boolean next();
    int getInt(String columnLabel);
    String getString(String columnLabel);
}