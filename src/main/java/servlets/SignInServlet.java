package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import com.google.gson.Gson;
import dbService.DBException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SignInServlet extends HttpServlet {
    private final AccountService accountService;

    public SignInServlet(AccountService as) {
        this.accountService = as;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        UserProfile userProfile;

        Map<String, String> options = new LinkedHashMap<>();
        String json;

        try {
            userProfile = accountService.getUser(login);

            if(userProfile.getPass().equals(password)) {
                options.put("status", "ok");
            }else{
                options.put("status", "unauthorized");
            }
        }catch(DBException e) {
            options.put("status", "error");
        }
        json = new Gson().toJson(options);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);

    }
}
