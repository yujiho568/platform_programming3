package edu.pnu.myjdbc.impl.mysql;

import edu.pnu.myjdbc.spec.ResultSet;
import edu.pnu.myjdbc.spec.Statement;

public class MySQLStatement implements Statement {
    @Override
    public ResultSet executeQuery(String query) {
        // Simulated query execution
        return new MySQLResultSet();
    }
}