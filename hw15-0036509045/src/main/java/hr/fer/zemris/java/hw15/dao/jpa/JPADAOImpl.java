package hr.fer.zemris.java.hw15.dao.jpa;

import hr.fer.zemris.java.hw15.dao.DAO;
import hr.fer.zemris.java.hw15.dao.DAOException;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * Implementation of {@link DAO} interface for JPA.
 */
public class JPADAOImpl implements DAO {

    @Override
    public BlogEntry getBlogEntry(Long id) throws DAOException {
        BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
        return blogEntry;
    }

    @Override
    public List<BlogUser> getBlogUsers() throws DAOException {
        return JPAEMProvider.getEntityManager()
                .createQuery("select user from BlogUser as user", BlogUser.class)
                .getResultList();
    }

    @Override
    public BlogUser getBlogUser(Long id) throws DAOException {
        return JPAEMProvider.getEntityManager().find(BlogUser.class, id);
    }

    @Override
    public BlogUser getBlogUserByNick(String nick) {
        BlogUser user = null;
        try {
            user = JPAEMProvider.getEntityManager()
                    .createNamedQuery("BlogUser.byNick", BlogUser.class)
                    .setParameter("nick", nick)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException ignorable) {

        }
        return user;
    }

    @Override
    public BlogUser getBlogUserByNickOrEmail(String nick, String email) {
        BlogUser user = null;
        try {
            user = JPAEMProvider.getEntityManager()
                    .createQuery("select u from BlogUser as u where u.email=:email or u.nick=:nick", BlogUser.class)
                    .setParameter("email", email)
                    .setParameter("nick", nick)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException ignorable) {

        }
        return user;
    }
}