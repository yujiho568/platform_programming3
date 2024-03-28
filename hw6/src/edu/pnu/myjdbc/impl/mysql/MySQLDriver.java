package edu.pnu.myjdbc.impl.mysql;

import edu.pnu.myjdbc.spec.Connection;
import edu.pnu.myjdbc.spec.Driver;

public class MySQLDriver implements Driver{
    @Override
    public boolean acceptsURL(String url){
        return url.startsWith("jdbc:mysql:");
    }

    @Override
    public Connection connect(String url, String user, String password) {
        return new MySQLConnection(url, user, password);
    }

}
