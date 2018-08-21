package cn.kgc.bankonline.service;

import cn.kgc.bankonline.dao.AccountDAO;
import cn.kgc.bankonline.dao.TransactionRecordDAO;
import cn.kgc.bankonline.entity.Account;
import cn.kgc.bankonline.entity.TransactionRecord;
import cn.kgc.bankonline.util.JdbcUtil;
import cn.kgc.bankonline.util.PageUtil;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

public class AccountService implements IAccountService {
    private AccountDAO accountDAO = new AccountDAO();
    private TransactionRecordDAO transactionRecordDAO = new TransactionRecordDAO();

    /**
     *
     * @param cardno
     * @param password
     * @return
     * @throws Exception
     */
    @Override
    public Account login(String cardno, String password) throws Exception {
        Account account = accountDAO.getAccountByCardno(cardno);
        if(account==null){
            throw new Exception("账号不存在");
        }else{
            if(account.getStatus()==0){
                throw new Exception("账号被冻结");
            }else{
                if(!account.getPassword().equals(password)){
                    throw new Exception("账号或密码错误");
                }
            }
        }
        return account;
    }

    /**
     *
     * @param cardno
     * @return
     */
    @Override
    public Double getBalance(String cardno) {
        return accountDAO.getAccountByCardno(cardno).getBalance();
    }

    /**
     *
     * @param cardno
     * @param money
     * @param toCardno
     * @return
     * @throws Exception
     */
    @Override
    public int tranferAccount(String cardno, Double money, String toCardno) throws Exception {
        Connection conn = JdbcUtil.getConn();
        //关闭自动提交
        conn.setAutoCommit(false);
        Account account1 = accountDAO.getAccountByCardno(cardno);
        Account account2 = accountDAO.getAccountByCardno(toCardno);
        //判断目标账号是否存在
        if(account2==null){
            throw new Exception("目标账号不存在");
        }else{
            if(account1.getBalance()<money){
                throw new Exception("余额不足");
            }else{
                //转账
                accountDAO.updateAccount(conn,cardno,(0-money));
                //添加转账记录
                TransactionRecord record1 = new TransactionRecord();
                record1.setCardno(cardno);
                record1.setExpense(money);
                record1.setTransaction_date(new Date());
                record1.setTransaction_type("转账");
                transactionRecordDAO.insertRecord(conn,record1);
                accountDAO.updateAccount(conn,toCardno,money);
                TransactionRecord record2 = new TransactionRecord();
                record2.setCardno(toCardno);
                record2.setIncome(money);
                record2.setTransaction_date(new Date());
                record2.setTransaction_type("入账");
                transactionRecordDAO.insertRecord(conn,record2);
            }
        }
        conn.commit();
        JdbcUtil.closeAll(null,null,conn);
        return 1;
    }

    /**
     *
     * @param cardno
     * @param startTime
     * @param endTime
     * @param index
     * @param size
     * @return
     */
    @Override
    public PageUtil<TransactionRecord> getRecord(String cardno, String startTime, String endTime, Integer index, Integer size) {
        List<TransactionRecord> records = transactionRecordDAO.getRecords(cardno,startTime,endTime,index,size);
        int total = transactionRecordDAO.getRecordCount(cardno,startTime,endTime);
        PageUtil<TransactionRecord> pageUtil = new PageUtil<>();
        pageUtil.index = index;
        pageUtil.size = size;
        pageUtil.total = total;
        //pageUtil.page = total%size==0?total/size:total/size+1;
        pageUtil.page = (total-1)/size+1;
        pageUtil.list = records;
        return pageUtil;
    }
    /**
     * 50   10      5
     * 51   10      6
     * 49   10      5
     */

    /**
     *
     * @param cardno
     * @param oldPass
     * @param newPass
     * @return
     */

    @Override
    public int modifiedPass(String cardno, String oldPass, String newPass) throws Exception {
        if(!accountDAO.getAccountByCardno(cardno).getPassword().equals(oldPass)){
            throw new Exception("旧密码错误");
        }
        return accountDAO.updateAccountPass(JdbcUtil.getConn(),cardno,newPass);
    }
}
