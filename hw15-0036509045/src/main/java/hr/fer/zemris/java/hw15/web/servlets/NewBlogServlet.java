package hr.fer.zemris.java.hw15.web.servlets;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.dao.jpa.JPAEMProvider;
import hr.fer.zemris.java.hw15.forms.BlogForm;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;
import hr.fer.zemris.java.hw15.services.Renderer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Servlet for creating a new blog.
 *
 * @author Jan Capek
 */
public class NewBlogServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        not logged in
        if (req.getSession().getAttribute("current.user.id") == null) {
            resp.setStatus(403);
            req.setAttribute("message", "To create new blog you need to log in.");
            Renderer.getInstance().render(req, resp, "mainLayout.jsp", "error.jsp");
            return;
        }
//        trying to create a blog on another author's behalf
        if (req.getSession().getAttribute("current.user.nick").equals(req.getAttribute("nick")) == false) {
            resp.setStatus(403);
            req.setAttribute("message", "You cannot add new blog on another author's behalf.");
            Renderer.getInstance().render(req, resp, "mainLayout.jsp", "error.jsp");
            return;
        }

        Renderer.getInstance().render(req, resp, "mainLayout.jsp", "createBlog.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (accessAllowed(req) == false) {
            resp.sendRedirect(req.getContextPath() + req.getServletPath() + (req.getPathInfo() == null ? "" : req.getPathInfo()));
        }

//        create form and check if it is ok
        BlogForm blogForm = new BlogForm(req.getParameter("title"), req.getParameter("text"));
        if (blogForm.isOk() == false) {
            req.setAttribute("blogForm", blogForm);
            doGet(req, resp);
            return;
        }

//        get logged user
        BlogUser user = DAOProvider.getDAO().getBlogUser((Long) req.getSession().getAttribute("current.user.id"));

//        create blog and save it
        BlogEntry blog = new BlogEntry();
        blog.setTitle(blogForm.getTitle());
        blog.setText(blogForm.getText());
        blog.setCreator(user);
        blog.setCreatedAt(new Date());
        blog.setLastModifiedAt(new Date());
        JPAEMProvider.getEntityManager().persist(blog);

//        redirect to user's page
        resp.sendRedirect(req.getContextPath() + "/servleti/author/" + user.getNick());
    }

    /**
     * Returns a flag that indicates if access is allowed or not.
     *
     * @param req Http request.
     * @return {@code true} if access is allowed, {@code false} otherwise.
     */
    private boolean accessAllowed(HttpServletRequest req) {
//        not logged in
        if (req.getSession().getAttribute("current.user.id") == null) {
            return false;
        }
//        trying to create a blog on another author's behalf
        if (req.getSession().getAttribute("current.user.nick").equals(req.getAttribute("nick")) == false) {
            return false;
        }
        return true;
    }
}
