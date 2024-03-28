package edu.pnu.myjdbc.impl.mysql;

import edu.pnu.myjdbc.spec.Connection;
import edu.pnu.myjdbc.spec.Statement;

// Located in edu.pnu.myjdbc.impl.mysql
public class MySQLConnection implements Connection{
    private final String databaseURL;
    private final String username;
    private final String password;

    public MySQLConnection(String databaseURL, String username, String password){
        this.databaseURL = databaseURL;
        this.username = username;
        this.password = password;
    }
    @Override
    public Statement createStatement(){
        return new MySQLStatement();
    }
}

