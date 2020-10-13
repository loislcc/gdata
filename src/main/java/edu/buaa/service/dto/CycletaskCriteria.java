package edu.buaa.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link edu.buaa.domain.Cycletask} entity. This class is used
 * in {@link edu.buaa.web.rest.CycletaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cycletasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CycletaskCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter cycle;

    private StringFilter nextime;

    private StringFilter nextendtime;

    private LongFilter taskid;

    public CycletaskCriteria(){
    }

    public CycletaskCriteria(CycletaskCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.cycle = other.cycle == null ? null : other.cycle.copy();
        this.nextime = other.nextime == null ? null : other.nextime.copy();
        this.nextendtime = other.nextendtime == null ? null : other.nextendtime.copy();
        this.taskid = other.taskid == null ? null : other.taskid.copy();
    }

    @Override
    public CycletaskCriteria copy() {
        return new CycletaskCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getCycle() {
        return cycle;
    }

    public void setCycle(StringFilter cycle) {
        this.cycle = cycle;
    }

    public StringFilter getNextime() {
        return nextime;
    }

    public void setNextime(StringFilter nextime) {
        this.nextime = nextime;
    }

    public StringFilter getNextendtime() {
        return nextendtime;
    }

    public void setNextendtime(StringFilter nextendtime) {
        this.nextendtime = nextendtime;
    }

    public LongFilter getTaskid() {
        return taskid;
    }

    public void setTaskid(LongFilter taskid) {
        this.taskid = taskid;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CycletaskCriteria that = (CycletaskCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(cycle, that.cycle) &&
            Objects.equals(nextime, that.nextime) &&
            Objects.equals(nextendtime, that.nextendtime) &&
            Objects.equals(taskid, that.taskid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        cycle,
        nextime,
        nextendtime,
        taskid
        );
    }

    @Override
    public String toString() {
        return "CycletaskCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (cycle != null ? "cycle=" + cycle + ", " : "") +
                (nextime != null ? "nextime=" + nextime + ", " : "") +
                (nextendtime != null ? "nextendtime=" + nextendtime + ", " : "") +
                (taskid != null ? "taskid=" + taskid + ", " : "") +
            "}";
    }

}
