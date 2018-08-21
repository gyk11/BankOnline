package cn.kgc.bankonline.entity;

public class Account {
    private String cardno;
    private String password;
    private Double balance;
    private Integer status;

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Account(String cardno, String password, Double balance, Integer status) {
        this.cardno = cardno;
        this.password = password;
        this.balance = balance;
        this.status = status;
    }

    public Account() {
    }

    @Override
    public String toString() {
        return "Account{" +
                "cardno='" + cardno + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", status=" + status +
                '}';
    }
}
