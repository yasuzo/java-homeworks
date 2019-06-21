package hr.fer.zemris.java.hw15.model;

import javax.persistence.*;
import java.util.List;

/**
 * Model of a blog user.
 *
 * @author Jan Capek
 */
@NamedQueries({
        @NamedQuery(name = "BlogUser.byNick", query = "select u from BlogUser as u where u.nick=:nick")
})
@Entity
@Table(name = "blog_users")
@Cacheable(false)
public class BlogUser {
    private Long id;
    private String firstName;
    private String lastName;
    private String nick;
    private String email;
    private String passwordHash;
    private List<BlogEntry> blogs;

    /**
     * Default constructor.
     */
    public BlogUser() {
    }

    /**
     * Constructs a new user with given attributes.
     *
     * @param firstName    User's first name.
     * @param lastName     User's last name.
     * @param nick         User's nickname.
     * @param email        User's email.
     * @param passwordHash User's password hash.
     */
    public BlogUser(String firstName, String lastName, String nick, String email, String passwordHash) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nick = nick;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    /**
     * @return Blogs created by the user.
     */
    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @OrderBy("lastModifiedAt desc")
    public List<BlogEntry> getBlogs() {
        return blogs;
    }

    /**
     * Sets user's blogs.
     *
     * @param blogs Blogs user has written.
     */
    public void setBlogs(List<BlogEntry> blogs) {
        this.blogs = blogs;
    }

    /**
     * @return User's id.
     */
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    /**
     * Sets user's id.
     *
     * @param id User's id.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return User's first name.
     */
    @Column(length = 50, nullable = false)
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets user's first name.
     *
     * @param firstName User's first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return User's last name.
     */
    @Column(length = 50, nullable = false)
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets user's last name.
     *
     * @param lastName User's last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return User's nickname.
     */
    @Column(length = 50, nullable = false, unique = true)
    public String getNick() {
        return nick;
    }

    /**
     * Sets user's nickname.
     *
     * @param nick User's nickname.
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * @return User's email address.
     */
    @Column(length = 50, nullable = false, unique = true)
    public String getEmail() {
        return email;
    }

    /**
     * Sets user's email address.
     *
     * @param email User's email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return User's password hash.
     */
    @Column(length = 40, nullable = false)
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets user's password hash.
     *
     * @param passwordHash Password hash.
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
