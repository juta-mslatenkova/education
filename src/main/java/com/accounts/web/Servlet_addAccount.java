package com.accounts.web;

import com.accounts.model.Account;
import com.accounts.utils.AccountFactory;
import com.accounts.utils.ServletUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class Servlet_addAccount extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        response.setContentType("text/html");

        String website = request.getParameter("website");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String database = request.getParameter("database");

        ServletUtil.setPropertiesFile(database, "SAVE", website, login, password);

        // now calling the Model class responsible for business logics
        AccountFactory.doAction();

        ArrayList<Account> accountsList = new ArrayList<>();
        accountsList.add(new Account(website, login, password));
        request.setAttribute("data", accountsList);

        // defining our view jsp.page
        RequestDispatcher view = request.getRequestDispatcher("result.jsp");
        view.forward(request, response);
    }
}
