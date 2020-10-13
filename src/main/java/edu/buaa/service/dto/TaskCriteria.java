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
 * Criteria class for the {@link edu.buaa.domain.Task} entity. This class is used
 * in {@link edu.buaa.web.rest.TaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TaskCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter type;

    private StringFilter startime;

    private StringFilter endtime;

    private StringFilter realtime;

    private StringFilter status;

    private StringFilter datanum;

    private StringFilter checknum;

    private StringFilter matrix;

    public TaskCriteria(){
    }

    public TaskCriteria(TaskCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.startime = other.startime == null ? null : other.startime.copy();
        this.endtime = other.endtime == null ? null : other.endtime.copy();
        this.realtime = other.realtime == null ? null : other.realtime.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.datanum = other.datanum == null ? null : other.datanum.copy();
        this.checknum = other.checknum == null ? null : other.checknum.copy();
        this.matrix = other.matrix == null ? null : other.matrix.copy();
    }

    @Override
    public TaskCriteria copy() {
        return new TaskCriteria(this);
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

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getStartime() {
        return startime;
    }

    public void setStartime(StringFilter startime) {
        this.startime = startime;
    }

    public StringFilter getEndtime() {
        return endtime;
    }

    public void setEndtime(StringFilter endtime) {
        this.endtime = endtime;
    }

    public StringFilter getRealtime() {
        return realtime;
    }

    public void setRealtime(StringFilter realtime) {
        this.realtime = realtime;
    }

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getDatanum() {
        return datanum;
    }

    public void setDatanum(StringFilter datanum) {
        this.datanum = datanum;
    }

    public StringFilter getChecknum() {
        return checknum;
    }

    public void setChecknum(StringFilter checknum) {
        this.checknum = checknum;
    }

    public StringFilter getMatrix() {
        return matrix;
    }

    public void setMatrix(StringFilter matrix) {
        this.matrix = matrix;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TaskCriteria that = (TaskCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(type, that.type) &&
            Objects.equals(startime, that.startime) &&
            Objects.equals(endtime, that.endtime) &&
            Objects.equals(realtime, that.realtime) &&
            Objects.equals(status, that.status) &&
            Objects.equals(datanum, that.datanum) &&
            Objects.equals(checknum, that.checknum) &&
            Objects.equals(matrix, that.matrix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        type,
        startime,
        endtime,
        realtime,
        status,
        datanum,
        checknum,
        matrix
        );
    }

    @Override
    public String toString() {
        return "TaskCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (startime != null ? "startime=" + startime + ", " : "") +
                (endtime != null ? "endtime=" + endtime + ", " : "") +
                (realtime != null ? "realtime=" + realtime + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (datanum != null ? "datanum=" + datanum + ", " : "") +
                (checknum != null ? "checknum=" + checknum + ", " : "") +
                (matrix != null ? "matrix=" + matrix + ", " : "") +
            "}";
    }

}
