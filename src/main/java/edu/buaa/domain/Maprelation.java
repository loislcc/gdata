package edu.buaa.domain;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Maprelation.
 */
@Entity
@Table(name = "maprelation")
public class Maprelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vnode")
    private String vnode;

    @Column(name = "rnode")
    private String rnode;

    @Column(name = "status")
    private String status;

    @Column(name = "size")
    private Double size;

    @Column(name = "lastime")
    private String lastime;

    @Column(name = "ip")
    private String ip;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVnode() {
        return vnode;
    }

    public Maprelation vnode(String vnode) {
        this.vnode = vnode;
        return this;
    }

    public void setVnode(String vnode) {
        this.vnode = vnode;
    }

    public String getRnode() {
        return rnode;
    }

    public Maprelation rnode(String rnode) {
        this.rnode = rnode;
        return this;
    }

    public void setRnode(String rnode) {
        this.rnode = rnode;
    }

    public String getStatus() {
        return status;
    }

    public Maprelation status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getSize() {
        return size;
    }

    public Maprelation size(Double size) {
        this.size = size;
        return this;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public String getLastime() {
        return lastime;
    }

    public Maprelation lastime(String lastime) {
        this.lastime = lastime;
        return this;
    }

    public void setLastime(String lastime) {
        this.lastime = lastime;
    }

    public String getIp() {
        return ip;
    }

    public Maprelation ip(String ip) {
        this.ip = ip;
        return this;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Maprelation)) {
            return false;
        }
        return id != null && id.equals(((Maprelation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Maprelation{" +
            "id=" + getId() +
            ", vnode='" + getVnode() + "'" +
            ", rnode='" + getRnode() + "'" +
            ", status='" + getStatus() + "'" +
            ", size=" + getSize() +
            ", lastime='" + getLastime() + "'" +
            ", ip='" + getIp() + "'" +
            "}";
    }
}
