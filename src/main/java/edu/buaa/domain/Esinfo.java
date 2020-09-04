package edu.buaa.domain;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Esinfo.
 */
@Entity
@Table(name = "esinfo")
public class Esinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "vnode")
    private String vnode;

    @Column(name = "rnode")
    private String rnode;

    @Column(name = "date")
    private String date;

    @Column(name = "pname")
    private String pname;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Esinfo name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public Esinfo type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVnode() {
        return vnode;
    }

    public Esinfo vnode(String vnode) {
        this.vnode = vnode;
        return this;
    }

    public void setVnode(String vnode) {
        this.vnode = vnode;
    }

    public String getRnode() {
        return rnode;
    }

    public Esinfo rnode(String rnode) {
        this.rnode = rnode;
        return this;
    }

    public void setRnode(String rnode) {
        this.rnode = rnode;
    }

    public String getDate() {
        return date;
    }

    public Esinfo date(String date) {
        this.date = date;
        return this;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPname() {
        return pname;
    }

    public Esinfo pname(String pname) {
        this.pname = pname;
        return this;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Esinfo)) {
            return false;
        }
        return id != null && id.equals(((Esinfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Esinfo{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", vnode='" + getVnode() + "'" +
            ", rnode='" + getRnode() + "'" +
            ", date='" + getDate() + "'" +
            ", pname='" + getPname() + "'" +
            "}";
    }
}
