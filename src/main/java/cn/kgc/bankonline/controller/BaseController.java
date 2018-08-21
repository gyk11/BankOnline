package cn.kgc.bankonline.controller;

import cn.kgc.bankonline.entity.Account;
import cn.kgc.bankonline.entity.TransactionRecord;
import cn.kgc.bankonline.service.AccountService;
import cn.kgc.bankonline.service.IAccountService;
import cn.kgc.bankonline.util.PageUtil;
import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BaseController extends HttpServlet {


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if("login".equals(action)){
            this.login(request,response);
        }else if("getBalance".equals(action)){
            this.findBalance(request,response);
        }else if("tranfer".equals(action)){
            this.tranfer(request,response);
        } else if("records".equals(action)){
            this.findRecords(request,response);
        } else if("modified".equals(action)){
            this.modifyPass(request,response);
        } else if("logout".equals(action)){
            this.logout(request,response);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().removeAttribute("account");
        request.getRequestDispatcher("login.jsp").forward(request,response);
    }

    private void modifyPass(HttpServletRequest request, HttpServletResponse response) throws IOException {
        IAccountService accountService = new AccountService();
        String cardno = ((Account)request.getSession().getAttribute("account")).getCardno();
        String oldPass = request.getParameter("oldPass");
        String newPass = request.getParameter("newPass");

        try {
            int result = accountService.modifiedPass(cardno,oldPass,newPass);
            response.getWriter().write(result+"");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.getWriter().write(e.getMessage());
        }
    }

    private void findRecords(HttpServletRequest request, HttpServletResponse response) throws IOException {
        IAccountService accountService = new AccountService();
        String cardno = ((Account)request.getSession().getAttribute("account")).getCardno();
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String indexStr = request.getParameter("index");
        Integer index = 1;
        if(indexStr!=null&&!"".equals(indexStr)){
            index = Integer.valueOf(indexStr);
        }
        PageUtil<TransactionRecord> pageUtil = accountService.getRecord(cardno,startTime,endTime,index,4);
        response.getWriter().write(JSON.toJSONString(pageUtil));
    }

    private void tranfer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        IAccountService accountService = new AccountService();
        String cardno = ((Account)request.getSession().getAttribute("account")).getCardno();
        String toCardno = request.getParameter("toCardno");
        String money = request.getParameter("money");
        try {
            int result = accountService.tranferAccount(cardno,Double.valueOf(money),toCardno);
            response.getWriter().write(result+"");
        } catch (Exception e) {
            System.err.print(e.getMessage());
            response.getWriter().write(e.getMessage());
        }
    }

    private void findBalance(HttpServletRequest request, HttpServletResponse response) throws IOException {
        IAccountService accountService = new AccountService();
        Account account = (Account) request.getSession().getAttribute("account");
        Double d  = accountService.getBalance(account.getCardno());
        response.getWriter().write(d+"");
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cardno = request.getParameter("cardno");
        String password = request.getParameter("password");
        IAccountService accountService = new AccountService();
        try {
            Account account = accountService.login(cardno,password);
            request.getSession().setAttribute("account",account);
            String jsonStr = JSON.toJSONString(account);
            response.getWriter().write(jsonStr);
        } catch (Exception e) {
            System.err.print(e.getMessage());
            response.getWriter().write(JSON.toJSONString(e.getMessage()));
        }

    }
}
