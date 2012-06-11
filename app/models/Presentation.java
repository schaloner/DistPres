package models;

import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

/**
 * @author Steve Chaloner
 */
@Entity
public class Presentation extends Model
{
    @Id
    public Long id;

    @Column(nullable = false)
    public String name;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Image> images;

    @Column(nullable = false)
    public Integer currentPosition;

    @Column(nullable = false)
    public Date uploadDate;

    public static final Finder<Long, Presentation> FIND = new Finder<Long, Presentation>(Long.class,
                                                                                         Presentation.class);

    private Presentation(Builder builder)
    {
        name = builder.name;
        images = builder.images;
        currentPosition = builder.currentPosition;
        uploadDate = builder.uploadDate;
    }

    public static final class Builder
    {
        private String name;
        private List<Image> images;
        private Integer currentPosition;
        private Date uploadDate;

        public Builder()
        {
        }

        public Builder name(String name)
        {
            this.name = name;
            return this;
        }

        public Builder images(List<Image> images)
        {
            this.images = images;
            return this;
        }

        public Builder currentPosition(Integer currentPosition)
        {
            this.currentPosition = currentPosition;
            return this;
        }

        public Builder uploadDate(Date uploadDate)
        {
            this.uploadDate = uploadDate;
            return this;
        }

        public Presentation build()
        {
            return new Presentation(this);
        }
    }
}
