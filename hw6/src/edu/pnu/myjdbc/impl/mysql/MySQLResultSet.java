package edu.pnu.myjdbc.impl.mysql;

// Located in edu.pnu.myjdbc.impl.mysql

import edu.pnu.myjdbc.spec.ResultSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MySQLResultSet implements ResultSet {

    private int cursor = -1;

    private final Map<String, Object>[] data = new Map[]{
            new HashMap<String, Object>(){{
                put("id",1);
                put("age",25);
                put("first", "Edward");
                put("last","Kim");
            }},
    };


    @Override
    public boolean next() {
        cursor++;
        return cursor < data.length;
    }

    @Override
    public int getInt(String columnLabel){
        return (int) data[cursor].get(columnLabel);
    }

    @Override
    public String getString(String columnLabel){
        return (String) data[cursor].get(columnLabel);
    }
}
