package hr.fer.zemris.java.hw15.web.servlets;

import hr.fer.zemris.java.hw15.PasswordHashUtility;
import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.dao.jpa.JPAEMProvider;
import hr.fer.zemris.java.hw15.forms.UserForm;
import hr.fer.zemris.java.hw15.model.BlogUser;
import hr.fer.zemris.java.hw15.services.Renderer;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Registration servlet.
 *
 * @author Jan Capek
 */
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        redirect if user has already logged in
        if (req.getSession().getAttribute("current.user.id") != null) {
            resp.sendRedirect(req.getContextPath() + "/servleti/author/" + req.getSession().getAttribute("current.user.nick"));
            return;
        }
        Renderer.getInstance().render(req, resp, "mainLayout.jsp", "registration.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        redirect if user has already logged in
        if (req.getSession().getAttribute("current.user.id") != null) {
            resp.sendRedirect(req.getContextPath() + "/servleti/author/" + req.getSession().getAttribute("current.user.nick"));
            return;
        }
//        create form
        UserForm form = new UserForm();
        form.setFirstName(req.getParameter("firstName"));
        form.setLastName(req.getParameter("lastName"));
        form.setEmail(req.getParameter("email"));
        form.setNick(req.getParameter("nick"));
        form.setPassword(req.getParameter("password"));

//        check if form data is valid
        if (form.isOk() == false) {
            req.setAttribute("userForm", form);
            Renderer.getInstance().render(req, resp, "mainLayout.jsp", "registration.jsp");
            return;
        }

//        check if user with gotten nickname or email already exists
        BlogUser existingUser = DAOProvider.getDAO().getBlogUserByNickOrEmail(form.getNick(), form.getEmail());
        if (existingUser != null) {
//            add appropriate messages
            if (existingUser.getEmail().equals(form.getEmail())) {
                form.addMessage("Email is taken.");
            }
            if (existingUser.getNick().equals(form.getNick())) {
                form.addMessage("Nickname is taken.");
            }

//            render registration.jsp
            req.setAttribute("userForm", form);
            Renderer.getInstance().render(req, resp, "mainLayout.jsp", "registration.jsp");
            return;
        }

//        create user and save it
        BlogUser user = new BlogUser(
                form.getFirstName(),
                form.getLastName(),
                form.getNick(),
                form.getEmail(),
                PasswordHashUtility.hashPassword(form.getPassword())
        );
        EntityManager entityManager = JPAEMProvider.getEntityManager();
        entityManager.persist(user);

//        flush to generate user id
        entityManager.flush();

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
