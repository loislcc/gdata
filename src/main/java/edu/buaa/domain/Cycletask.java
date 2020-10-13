package edu.buaa.domain;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Cycletask.
 */
@Entity
@Table(name = "cycletask")
public class Cycletask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "cycle")
    private String cycle;

    @Column(name = "nextime")
    private String nextime;

    @Column(name = "nextendtime")
    private String nextendtime;

    @Column(name = "taskid")
    private Long taskid;

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

    public Cycletask name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCycle() {
        return cycle;
    }

    public Cycletask cycle(String cycle) {
        this.cycle = cycle;
        return this;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getNextime() {
        return nextime;
    }

    public Cycletask nextime(String nextime) {
        this.nextime = nextime;
        return this;
    }

    public void setNextime(String nextime) {
        this.nextime = nextime;
    }

    public String getNextendtime() {
        return nextendtime;
    }

    public Cycletask nextendtime(String nextendtime) {
        this.nextendtime = nextendtime;
        return this;
    }

    public void setNextendtime(String nextendtime) {
        this.nextendtime = nextendtime;
    }

    public Long getTaskid() {
        return taskid;
    }

    public Cycletask taskid(Long taskid) {
        this.taskid = taskid;
        return this;
    }

    public void setTaskid(Long taskid) {
        this.taskid = taskid;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cycletask)) {
            return false;
        }
        return id != null && id.equals(((Cycletask) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Cycletask{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", cycle='" + getCycle() + "'" +
            ", nextime='" + getNextime() + "'" +
            ", nextendtime='" + getNextendtime() + "'" +
            ", taskid=" + getTaskid() +
            "}";
    }
}
