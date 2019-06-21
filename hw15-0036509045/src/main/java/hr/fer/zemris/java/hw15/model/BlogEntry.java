package hr.fer.zemris.java.hw15.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model of the blog entry.
 */
@NamedQueries({
        @NamedQuery(name = "BlogEntry.byId", query = "select b from BlogComment as b where b.blogEntry=:be and b.postedOn>:when")
})
@Entity
@Table(name = "blog_entries")
@Cacheable(false)
public class BlogEntry {

    private Long id;
    private List<BlogComment> comments = new ArrayList<>();
    private Date createdAt;
    private Date lastModifiedAt;
    private String title;
    private String text;

    /**
     * Author of the blog
     **/
    private BlogUser creator;

    /**
     * @return Blog's id.
     */
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    /**
     * Sets blog's id.
     *
     * @param id Blog's id.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return List of all comments on the blog.
     */
    @OneToMany(mappedBy = "blogEntry", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @OrderBy("postedOn")
    public List<BlogComment> getComments() {
        return comments;
    }

    /**
     * @param comments Sets new list of comments.
     */
    public void setComments(List<BlogComment> comments) {
        this.comments = comments;
    }

    /**
     * @return Date of blog's creation.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt Date of blog's creation.
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return Last modification date.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    /**
     * Sets last modification date.
     *
     * @param lastModifiedAt New modification date.
     */
    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    @Column(length = 200, nullable = false)
    public String getTitle() {
        return title;
    }

    /**
     * Sets a new title of the blog.
     *
     * @param title Blog's title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Blog text.
     */
    @Column(length = 4096, nullable = false)
    public String getText() {
        return text;
    }

    /**
     * Sets a new blog text.
     *
     * @param text New text.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return Creator of the blog.
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    public BlogUser getCreator() {
        return creator;
    }

    /**
     * Sets blog's creator.
     *
     * @param creator Creator.
     */
    public void setCreator(BlogUser creator) {
        this.creator = creator;
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
        BlogEntry other = (BlogEntry) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}