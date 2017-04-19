package servlets;

import accounts.AccountService;
import com.google.gson.Gson;
import dbService.DBException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SignUpServlet extends HttpServlet {
    private final AccountService accountService;

    public SignUpServlet(AccountService as){
        this.accountService = as;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        Map<String, String> options = new LinkedHashMap<>();
        String json;
        try {
            accountService.addUser(login, password);
            options.put("status", "ok");
        }catch (DBException e){
            options.put("status", "error");
        }
        json = new Gson().toJson(options);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }
}
