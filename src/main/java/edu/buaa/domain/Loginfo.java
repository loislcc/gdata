package edu.buaa.domain;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Loginfo.
 */
@Entity
@Table(name = "loginfo")
public class Loginfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ip")
    private String ip;

    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;

    @Column(name = "x")
    private Double x;

    @Column(name = "y")
    private Double y;

    @Column(name = "eventime")
    private String eventime;

    @Column(name = "note")
    private String note;

    @Column(name = "owner")
    private String owner;

    @Column(name = "level")
    private String level;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public Loginfo ip(String ip) {
        this.ip = ip;
        return this;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getType() {
        return type;
    }

    public Loginfo type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Loginfo name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getX() {
        return x;
    }

    public Loginfo x(Double x) {
        this.x = x;
        return this;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public Loginfo y(Double y) {
        this.y = y;
        return this;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public String getEventime() {
        return eventime;
    }

    public Loginfo eventime(String eventime) {
        this.eventime = eventime;
        return this;
    }

    public void setEventime(String eventime) {
        this.eventime = eventime;
    }

    public String getNote() {
        return note;
    }

    public Loginfo note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOwner() {
        return owner;
    }

    public Loginfo owner(String owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLevel() {
        return level;
    }

    public Loginfo level(String level) {
        this.level = level;
        return this;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Loginfo)) {
            return false;
        }
        return id != null && id.equals(((Loginfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Loginfo{" +
            "id=" + getId() +
            ", ip='" + getIp() + "'" +
            ", type='" + getType() + "'" +
            ", name='" + getName() + "'" +
            ", x=" + getX() +
            ", y=" + getY() +
            ", eventime='" + getEventime() + "'" +
            ", note='" + getNote() + "'" +
            ", owner='" + getOwner() + "'" +
            ", level='" + getLevel() + "'" +
            "}";
    }
}
