package hr.fer.zemris.java.hw15.web.servlets;

import hr.fer.zemris.java.hw15.PasswordHashUtility;
import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.forms.LoginForm;
import hr.fer.zemris.java.hw15.model.BlogUser;
import hr.fer.zemris.java.hw15.services.Renderer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet that handles request to the main page.
 *
 * @author Jan Capek
 */
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("authors", DAOProvider.getDAO().getBlogUsers());
        Renderer.getInstance().render(req, resp, "mainLayout.jsp", "main.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("current.user.id") != null) {
            resp.sendRedirect(req.getContextPath() + "/servleti/main");
            return;
        }

        LoginForm loginForm = new LoginForm(req.getParameter("nick"), req.getParameter("password"));

        if (loginForm.isOk() == false) {
            req.setAttribute("loginForm", loginForm);
            doGet(req, resp);
            return;
        }

//        find user by entered nick
        BlogUser user = DAOProvider.getDAO().getBlogUserByNick(loginForm.getNick());

//        check if user with entered nickname exists and that their password is valid
        if (user == null || PasswordHashUtility.matchToHash(loginForm.getPassword(), user.getPasswordHash()) == false) {
            loginForm.addMessage("Nick or password is invalid.");
            req.setAttribute("loginForm", loginForm);
            doGet(req, resp);
            return;
        }

//        todo: Extract this to session wrapper (create one lol)
//        log user in
        HttpSession session = req.getSession();
        session.setAttribute("current.user.id", user.getId());
        session.setAttribute("current.user.firstName", user.getFirstName());
        session.setAttribute("current.user.lastName", user.getLastName());
        session.setAttribute("current.user.nick", user.getNick());

//        redirect user to their page
        resp.sendRedirect(req.getContextPath() + "/servleti/author/" + user.getNick());
    }
}
