package edu.pnu.myjdbc.spec;

public interface Driver {
    boolean acceptsURL(String url);
    Connection connect(String url, String user, String password);
}