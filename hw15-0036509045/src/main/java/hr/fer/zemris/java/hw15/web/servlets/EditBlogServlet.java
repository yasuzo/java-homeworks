package hr.fer.zemris.java.hw15.web.servlets;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.forms.BlogForm;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.services.Renderer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Servlet for blog editing.
 *
 * @author Jan Capek
 */
public class EditBlogServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlogEntry blogEntry = getBlog(req, resp);
        if (blogEntry == null) {
            return;
        }

        req.setAttribute("blogForm", new BlogForm(blogEntry.getTitle(), blogEntry.getText()));
        Renderer.getInstance().render(req, resp, "mainLayout.jsp", "editBlog.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlogEntry blogEntry = getBlog(req, resp);
        if (blogEntry == null) {
            return;
        }

//        create blog form and check its data
        BlogForm blogForm = new BlogForm(req.getParameter("title"), req.getParameter("text"));
        if (blogForm.isOk() == false) {
            req.setAttribute("blogForm", blogForm);
            Renderer.getInstance().render(req, resp, "mainLayout.jsp", "editBlog.jsp");
            return;
        }

        blogEntry.setText(blogForm.getText());
        blogEntry.setTitle(blogForm.getTitle());
        blogEntry.setLastModifiedAt(new Date());

        resp.sendRedirect(req.getContextPath() + "/servleti/author/" + req.getSession().getAttribute("current.user.nick"));
    }


    /**
     * Performs authorization check and returns {@link BlogEntry} if user is authorized to edit requested blog.
     * If user is unauthorized appropriate message will be rendered to user.
     *
     * @param req  Request.
     * @param resp Response.
     * @return Blog entry if user is authorized to edit requested blog; {@code null} otherwise.
     * @throws ServletException In case of an error.
     * @throws IOException      If data could not be read/written.
     */
    private BlogEntry getBlog(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        not logged in
        if (req.getSession().getAttribute("current.user.id") == null) {
            renderErrorMessage(req, resp, 403, "To edit a blog you need to log in.");
            return null;
        }
//        trying to edit another author's blog
        if (req.getSession().getAttribute("current.user.nick").equals(req.getAttribute("nick")) == false) {
            renderErrorMessage(req, resp, 403, "You cannot add edit another author's blog.");
            return null;
        }

//        parse blog id and fetch blog
        long blogId;
        try {
            blogId = Long.parseLong((String) req.getAttribute("blogId"));
        } catch (NumberFormatException e) {
            renderErrorMessage(req, resp, 200, "Invalid blog id.");
            return null;
        }
        BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(blogId);

//        blog with given id does not exist
        if (blogEntry == null) {
            renderErrorMessage(req, resp, 200, "Blog with given id does not exist.");
            return null;
        }

//        blog that should be edited does not belong to logged user
        if (blogEntry.getCreator().getNick().equals(req.getSession().getAttribute("current.user.nick")) == false) {
            renderErrorMessage(req, resp, 403, "Not authorized to edit blog.");
            return null;
        }

        return blogEntry;
    }

    /**
     * Renders an error message to user.
     *
     * @param req        Request.
     * @param resp       Response.
     * @param statusCode Response status code.
     * @param message    Error message.
     * @throws ServletException In case of an error.
     * @throws IOException      If data could not be written/read.
     */
    private void renderErrorMessage(HttpServletRequest req, HttpServletResponse resp, int statusCode, String message) throws ServletException, IOException {
        resp.setStatus(403);
        req.setAttribute("message", "You cannot add edit another author's blog.");
        Renderer.getInstance().render(req, resp, "mainLayout.jsp", "error.jsp");
    }
}
