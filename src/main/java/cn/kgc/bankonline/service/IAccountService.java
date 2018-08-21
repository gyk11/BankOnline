package cn.kgc.bankonline.service;

import cn.kgc.bankonline.entity.Account;
import cn.kgc.bankonline.entity.TransactionRecord;
import cn.kgc.bankonline.util.PageUtil;

public interface IAccountService {
    public Account login(String cardno,String password) throws Exception;

    public Double getBalance(String cardno);

    public int tranferAccount(String cardno,Double money,String toCardno) throws Exception;

    public PageUtil<TransactionRecord> getRecord(String cardno,String startTime,String endTime,Integer index,Integer size);

    public int modifiedPass(String cardno,String oldPass,String newPass) throws Exception;
}
