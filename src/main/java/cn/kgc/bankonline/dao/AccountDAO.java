package cn.kgc.bankonline.dao;

import cn.kgc.bankonline.entity.Account;

import java.sql.Connection;

public class AccountDAO extends BaseDAO {

    public Account getAccountByCardno(String cardno){
        String sql = "select * from account where cardno = ?";
        return  super.queryOne(Account.class,sql,cardno);
    }

    public int updateAccount(Connection conn, String cardno, double money){
        String sql = "update account set balance = balance + ? where cardno = ?";
        return super.update(conn,sql,money,cardno);
    }

    public int updateAccountPass(Connection conn,String cardno, String password){
        String sql = "update account set password = ? where cardno = ?";
        return super.update(conn,sql,password,cardno);
    }
}
