package hr.fer.zemris.java.hw15.dao;

import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

import java.util.List;

/**
 * Data access interface is used to fetch data from external sources.
 */
public interface DAO {

    /**
     * Dohvaća entry sa zadanim <code>id</code>-em. Ako takav entry ne postoji,
     * vraća <code>null</code>.
     *
     * @param id ključ zapisa
     * @return entry ili <code>null</code> ako entry ne postoji
     * @throws DAOException ako dođe do pogreške pri dohvatu podataka
     */
    BlogEntry getBlogEntry(Long id) throws DAOException;

    /**
     * Fetches all registered blog users.
     *
     * @return List of blog users.
     * @throws DAOException In case of an error while fetching data.
     */
    List<BlogUser> getBlogUsers() throws DAOException;

    /**
     * Fetches a blog user with given id.
     *
     * @return Blog user if one exists, {@code null} otherwise.
     * @throws DAOException In case of an error while fetching data.
     */
    BlogUser getBlogUser(Long id) throws DAOException;

    /**
     * Fetches a blog user with given nick.
     *
     * @return Blog user if one exists, {@code null} otherwise.
     * @throws DAOException In case of an error while fetching data.
     */
    BlogUser getBlogUserByNick(String nick);

    /**
     * Fetches a blog user with given nick OR email.
     * This will return ONLY one user.
     *
     * @return Blog user if one exists, {@code null} otherwise.
     * @throws DAOException In case of an error while fetching data.
     */
    BlogUser getBlogUserByNickOrEmail(String nick, String email);

}