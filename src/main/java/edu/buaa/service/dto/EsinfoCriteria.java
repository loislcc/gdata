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
 * Criteria class for the {@link edu.buaa.domain.Esinfo} entity. This class is used
 * in {@link edu.buaa.web.rest.EsinfoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /esinfos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EsinfoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter type;

    private StringFilter vnode;

    private StringFilter rnode;

    private StringFilter date;

    private StringFilter pname;

    public EsinfoCriteria(){
    }

    public EsinfoCriteria(EsinfoCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.vnode = other.vnode == null ? null : other.vnode.copy();
        this.rnode = other.rnode == null ? null : other.rnode.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.pname = other.pname == null ? null : other.pname.copy();
    }

    @Override
    public EsinfoCriteria copy() {
        return new EsinfoCriteria(this);
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

    public StringFilter getDate() {
        return date;
    }

    public void setDate(StringFilter date) {
        this.date = date;
    }

    public StringFilter getPname() {
        return pname;
    }

    public void setPname(StringFilter pname) {
        this.pname = pname;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EsinfoCriteria that = (EsinfoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(type, that.type) &&
            Objects.equals(vnode, that.vnode) &&
            Objects.equals(rnode, that.rnode) &&
            Objects.equals(date, that.date) &&
            Objects.equals(pname, that.pname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        type,
        vnode,
        rnode,
        date,
        pname
        );
    }

    @Override
    public String toString() {
        return "EsinfoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (vnode != null ? "vnode=" + vnode + ", " : "") +
                (rnode != null ? "rnode=" + rnode + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (pname != null ? "pname=" + pname + ", " : "") +
            "}";
    }

}
