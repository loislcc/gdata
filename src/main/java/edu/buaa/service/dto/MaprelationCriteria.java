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
 * Criteria class for the {@link edu.buaa.domain.Maprelation} entity. This class is used
 * in {@link edu.buaa.web.rest.MaprelationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /maprelations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MaprelationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter vnode;

    private StringFilter rnode;

    private StringFilter status;

    private DoubleFilter size;

    public MaprelationCriteria(){
    }

    public MaprelationCriteria(MaprelationCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.vnode = other.vnode == null ? null : other.vnode.copy();
        this.rnode = other.rnode == null ? null : other.rnode.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.size = other.size == null ? null : other.size.copy();
    }

    @Override
    public MaprelationCriteria copy() {
        return new MaprelationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getVnode() {
        return vnode;
    }

    public void setVnode(StringFilter vnode) {
        this.vnode = vnode;
    }

    public StringFilter getRnode() {
        return rnode;
    }

    public void setRnode(StringFilter rnode) {
        this.rnode = rnode;
    }

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public DoubleFilter getSize() {
        return size;
    }

    public void setSize(DoubleFilter size) {
        this.size = size;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MaprelationCriteria that = (MaprelationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(vnode, that.vnode) &&
            Objects.equals(rnode, that.rnode) &&
            Objects.equals(status, that.status) &&
            Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        vnode,
        rnode,
        status,
        size
        );
    }

    @Override
    public String toString() {
        return "MaprelationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (vnode != null ? "vnode=" + vnode + ", " : "") +
                (rnode != null ? "rnode=" + rnode + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (size != null ? "size=" + size + ", " : "") +
            "}";
    }

}
