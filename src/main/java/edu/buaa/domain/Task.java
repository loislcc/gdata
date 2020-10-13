package edu.buaa.domain;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "startime")
    private String startime;

    @Column(name = "endtime")
    private String endtime;

    @Column(name = "realtime")
    private String realtime;

    @Column(name = "status")
    private String status;

    @Column(name = "datanum")
    private String datanum;

    @Column(name = "checknum")
    private String checknum;

    @Column(name = "matrix")
    private String matrix;

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

    public Task name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public Task type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartime() {
        return startime;
    }

    public Task startime(String startime) {
        this.startime = startime;
        return this;
    }

    public void setStartime(String startime) {
        this.startime = startime;
    }

    public String getEndtime() {
        return endtime;
    }

    public Task endtime(String endtime) {
        this.endtime = endtime;
        return this;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getRealtime() {
        return realtime;
    }

    public Task realtime(String realtime) {
        this.realtime = realtime;
        return this;
    }

    public void setRealtime(String realtime) {
        this.realtime = realtime;
    }

    public String getStatus() {
        return status;
    }

    public Task status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatanum() {
        return datanum;
    }

    public Task datanum(String datanum) {
        this.datanum = datanum;
        return this;
    }

    public void setDatanum(String datanum) {
        this.datanum = datanum;
    }

    public String getChecknum() {
        return checknum;
    }

    public Task checknum(String checknum) {
        this.checknum = checknum;
        return this;
    }

    public void setChecknum(String checknum) {
        this.checknum = checknum;
    }

    public String getMatrix() {
        return matrix;
    }

    public Task matrix(String matrix) {
        this.matrix = matrix;
        return this;
    }

    public void setMatrix(String matrix) {
        this.matrix = matrix;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        return id != null && id.equals(((Task) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", startime='" + getStartime() + "'" +
            ", endtime='" + getEndtime() + "'" +
            ", realtime='" + getRealtime() + "'" +
            ", status='" + getStatus() + "'" +
            ", datanum='" + getDatanum() + "'" +
            ", checknum='" + getChecknum() + "'" +
            ", matrix='" + getMatrix() + "'" +
            "}";
    }
}
