package cn.kgc.bankonline.dao;

import cn.kgc.bankonline.entity.TransactionRecord;
import cn.kgc.bankonline.util.JdbcUtil;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TransactionRecordDAO extends BaseDAO {

    public int insertRecord(Connection con, TransactionRecord record){
        String sql = "insert into transaction_record (" +
                "cardno,transaction_date," +
                "expense,income,balance," +
                "transaction_type,remark)" +
                "values (?,?,?,?,?,?,?)";
        return super.update(con,sql,record.getCardno(),record.getTransaction_date()
        ,record.getExpense(),record.getIncome(),record.getBalance()
        ,record.getTransaction_type(),record.getRemark());
    }

    public List<TransactionRecord> getRecords(String cardno,String startTime,String endTime,Integer index,Integer size){
        List<Object> params = new ArrayList<>();
        String sql = "select * from transaction_record" +
                " where cardno = ?";
        params.add(cardno);
        if(startTime!=null&&!"".equals(startTime)){
            sql+=" and date_format(transaction_date,'%Y-%m-%d')>=?";
            params.add(startTime);
        }
        if(endTime!=null&&!"".equals(endTime)){
            sql+=" and date_format(transaction_date,'%Y-%m-%d')<=?";
            params.add(endTime);
        }
        sql+=" limit ?,?";
        params.add((index-1)*size);
        params.add(size);
        return super.query(TransactionRecord.class,sql,params.toArray());
    }
    public int getRecordCount(String cardno,String startTime,String endTime){
        List<Object> params = new ArrayList<>();
        String sql = "select count(1) c from transaction_record" +
                " where cardno = ?";
        params.add(cardno);
        if(startTime!=null&&!"".equals(startTime)){
            sql+=" and date_format(transaction_date,'%Y-%m-%d')>=?";
            params.add(startTime);
        }
        if(endTime!=null&&!"".equals(endTime)){
            sql+=" and date_format(transaction_date,'%Y-%m-%d')<=?";
            params.add(endTime);
        }
        Map<String,Object> map = super.queryMap(sql,params.toArray()).get(0);
        return Integer.valueOf(map.get("c").toString()) ;
    }

    /**
     * 动态sql
     * 动态参数
     */

    public static void main(String[] args) throws ParseException {
        TransactionRecordDAO dao = new TransactionRecordDAO();
        TransactionRecord record = new TransactionRecord();
        record.setCardno("1");
        //dao.insertRecord(JdbcUtil.getConn(),record);
        List<TransactionRecord> list = dao.getRecords("1","2018-08-19","2018-08-20",1,3);
        int total = dao.getRecordCount("1","2018-08-19","2018-08-20");
        System.out.println(list);
        System.out.println(total);

    }
}
