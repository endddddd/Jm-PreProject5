package servlet;

import exceptions.DBException;
import model.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    UserService userService = UserService.getInstance();


    @Override
    public void init() {
        try {
            // создаем таблицу и пользователей для входа в систему
            userService.createTable();
            userService.addUser(new User("admin", "admin", "admin"));
            userService.addUser(new User("user", "user", "user"));
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        HttpSession session = req.getSession();

        String name = req.getParameter("name");
        String pass = req.getParameter("pass");

        User user = userService.validateUser(name, pass);
        if (user == null) {
            doGet(req, resp);
        } else if (user.getRole().equals("admin")) {
            session.setAttribute("admin", name);
            resp.sendRedirect(req.getContextPath() + "/admin/table");
        } else if (user.getRole().equals("user")) {
            session.setAttribute("user", name);
            resp.sendRedirect(req.getContextPath() + "/user/");
        }

    }
}
