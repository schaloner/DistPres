package models;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Steve Chaloner
 */
@Entity
public class BinaryContent extends Model
{
    @Id
    public Long id;

    @Column(nullable = false, length = 2524288)
    public byte[] bytes;

    private BinaryContent(Builder builder)
    {
        bytes = builder.bytes;
    }

    public static final class Builder
    {
        private byte[] bytes;

        public Builder()
        {
        }

        public Builder bytes(byte[] bytes)
        {
            this.bytes = bytes;
            return this;
        }

        public BinaryContent build()
        {
            return new BinaryContent(this);
        }
    }
}
