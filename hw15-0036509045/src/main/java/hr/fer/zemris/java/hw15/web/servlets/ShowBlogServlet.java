package hr.fer.zemris.java.hw15.web.servlets;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.dao.jpa.JPAEMProvider;
import hr.fer.zemris.java.hw15.forms.CommentForm;
import hr.fer.zemris.java.hw15.model.BlogComment;
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
 * Displays requested blog and its comments.
 * This will also handle posting of the comments.
 *
 * @author Jan Capek
 */
public class ShowBlogServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlogEntry blog = getBlog(req, resp);
        if (blog == null) {
            return;
        }

        req.setAttribute("blog", blog);
        req.setAttribute("showEditOption", blog.getCreator().getNick().equals(req.getSession().getAttribute("current.user.nick")));
        Renderer.getInstance().render(req, resp, "mainLayout.jsp", "blog.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlogEntry blog = getBlog(req, resp);
        if (blog == null) {
            return;
        }

        BlogUser user = null;
        if (req.getSession().getAttribute("current.user.id") != null) {
            user = DAOProvider.getDAO().getBlogUser((Long) req.getSession().getAttribute("current.user.id"));
        }

//        create form and check its vailidity
        CommentForm commentForm = new CommentForm(
                user == null ? req.getParameter("email") : user.getEmail(),
                req.getParameter("message")
        );
        if (commentForm.isOk() == false) {
            req.setAttribute("commentForm", commentForm);
            doGet(req, resp);
            return;
        }

        BlogComment comment = new BlogComment();
        comment.setBlogEntry(blog);
        comment.setMessage(commentForm.getMessage());
        comment.setPostedOn(new Date());
        comment.setUsersEMail(commentForm.getEmail());

        JPAEMProvider.getEntityManager().persist(comment);

        resp.sendRedirect(req.getContextPath() + "/servleti/author/" + req.getAttribute("nick") + "/" + req.getAttribute("blogId"));
    }

    /**
     * Returns blog which user has requested or if they requested invalid blog, appropriate message will be rendered.
     *
     * @param req  Http request.
     * @param resp Http response.
     * @return Blog entry user requested; {@code null} otherwise.
     * @throws ServletException In case of an error.
     * @throws IOException      If data could not be read/written.
     */
    private static BlogEntry getBlog(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long blogId;
        try {
            blogId = Long.parseLong((String) req.getAttribute("blogId"));
        } catch (NumberFormatException e) {
            req.setAttribute("message", "Requested blog does not exist.");
            Renderer.getInstance().render(req, resp, "mainLayout.jsp", "error.jsp");
            return null;
        }

        BlogEntry blog = DAOProvider.getDAO().getBlogEntry(blogId);
        if (blog == null || blog.getCreator().getNick().equals(req.getAttribute("nick")) == false) {
            req.setAttribute("message", "Requested blog does not exist.");
            Renderer.getInstance().render(req, resp, "mainLayout.jsp", "error.jsp");
            return null;
        }
        return blog;
    }
}
