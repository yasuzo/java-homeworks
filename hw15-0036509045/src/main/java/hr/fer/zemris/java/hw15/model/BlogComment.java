package hr.fer.zemris.java.hw15.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Model of the comment for a blog.
 */
@Entity
@Table(name = "blog_comments")
public class BlogComment {

    private Long id;
    private BlogEntry blogEntry;
    private String usersEMail;
    private String message;
    private Date postedOn;

    /**
     * @return Comment id.
     */
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    /**
     * Sets a new id for the comment.
     *
     * @param id Comment's id.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Blog on which this comment is on.
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    public BlogEntry getBlogEntry() {
        return blogEntry;
    }

    /**
     * Sets a blog this comment is on.
     *
     * @param blogEntry Blog this comment is on.
     */
    public void setBlogEntry(BlogEntry blogEntry) {
        this.blogEntry = blogEntry;
    }

    /**
     * @return E-mail of the comment creator.
     */
    @Column(length = 100, nullable = false)
    public String getUsersEMail() {
        return usersEMail;
    }

    /**
     * Sets an e-mail of the comment creator.
     *
     * @param usersEMail User's email.
     */
    public void setUsersEMail(String usersEMail) {
        this.usersEMail = usersEMail;
    }

    /**
     * @return Comment text.
     */
    @Column(length = 4096, nullable = false)
    public String getMessage() {
        return message;
    }

    /**
     * Sets comment text.
     *
     * @param message Comment text.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return Date of the post of the comments.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date getPostedOn() {
        return postedOn;
    }

    /**
     * Sets date this comment is posted on.
     *
     * @param postedOn Date of post of the comment.
     */
    public void setPostedOn(Date postedOn) {
        this.postedOn = postedOn;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BlogComment other = (BlogComment) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}