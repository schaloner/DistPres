package models;

import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * @author Steve Chaloner
 */
@Entity
public class Image extends Model
{
    @Id
    public Long id;

    @Column(nullable = false)
    public String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public BinaryContent binaryContent;

    private Image(Builder builder)
    {
        name = builder.name;
        binaryContent = builder.binaryContent;
    }

    public static final class Builder
    {
        private String name;
        private BinaryContent binaryContent;

        public Builder()
        {
        }

        public Builder name(String name)
        {
            this.name = name;
            return this;
        }

        public Builder binaryContent(BinaryContent binaryContent)
        {
            this.binaryContent = binaryContent;
            return this;
        }

        public Image build()
        {
            return new Image(this);
        }
    }
}
