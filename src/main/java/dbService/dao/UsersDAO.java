package dbService.dao;

import accounts.AccountService;
import accounts.UserProfile;
import dbService.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;


public class UsersDAO {
    private Executor executor;

    public UsersDAO(Connection connection){
        this.executor = new Executor(connection);
    }

    public UserProfile get(String name) throws SQLException {
        return executor.execQuery("select * from users where login='" + name+ "'", result -> {
            result.next();
            return new UserProfile(result.getLong(1), result.getString(2), result.getString(3));
        });
    }


    public void insertUser(String name, String password) throws SQLException {
        executor.execUpdate("insert into users (login, password) values ('" + name + "', '" + password + "')");
    }

    public void createTable() throws SQLException {
        executor.execUpdate("create table if not exists users (id bigint auto_increment, login varchar(256), password varchar(256), primary key (id))");
    }

    public void dropTable() throws SQLException {
        executor.execUpdate("drop table users");
    }
}
