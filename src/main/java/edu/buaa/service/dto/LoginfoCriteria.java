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
 * Criteria class for the {@link edu.buaa.domain.Loginfo} entity. This class is used
 * in {@link edu.buaa.web.rest.LoginfoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /loginfos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LoginfoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ip;

    private StringFilter type;

    private StringFilter name;

    private DoubleFilter x;

    private DoubleFilter y;

    private StringFilter eventime;

    private StringFilter note;

    private StringFilter owner;

    private StringFilter level;

    public LoginfoCriteria(){
    }

    public LoginfoCriteria(LoginfoCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.ip = other.ip == null ? null : other.ip.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.x = other.x == null ? null : other.x.copy();
        this.y = other.y == null ? null : other.y.copy();
        this.eventime = other.eventime == null ? null : other.eventime.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.owner = other.owner == null ? null : other.owner.copy();
        this.level = other.level == null ? null : other.level.copy();
    }

    @Override
    public LoginfoCriteria copy() {
        return new LoginfoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getIp() {
        return ip;
    }

    public void setIp(StringFilter ip) {
        this.ip = ip;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public DoubleFilter getX() {
        return x;
    }

    public void setX(DoubleFilter x) {
        this.x = x;
    }

    public DoubleFilter getY() {
        return y;
    }

    public void setY(DoubleFilter y) {
        this.y = y;
    }

    public StringFilter getEventime() {
        return eventime;
    }

    public void setEventime(StringFilter eventime) {
        this.eventime = eventime;
    }

    public StringFilter getNote() {
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public StringFilter getOwner() {
        return owner;
    }

    public void setOwner(StringFilter owner) {
        this.owner = owner;
    }

    public StringFilter getLevel() {
        return level;
    }

    public void setLevel(StringFilter level) {
        this.level = level;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LoginfoCriteria that = (LoginfoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(ip, that.ip) &&
            Objects.equals(type, that.type) &&
            Objects.equals(name, that.name) &&
            Objects.equals(x, that.x) &&
            Objects.equals(y, that.y) &&
            Objects.equals(eventime, that.eventime) &&
            Objects.equals(note, that.note) &&
            Objects.equals(owner, that.owner) &&
            Objects.equals(level, that.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        ip,
        type,
        name,
        x,
        y,
        eventime,
        note,
        owner,
        level
        );
    }

    @Override
    public String toString() {
        return "LoginfoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (ip != null ? "ip=" + ip + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (x != null ? "x=" + x + ", " : "") +
                (y != null ? "y=" + y + ", " : "") +
                (eventime != null ? "eventime=" + eventime + ", " : "") +
                (note != null ? "note=" + note + ", " : "") +
                (owner != null ? "owner=" + owner + ", " : "") +
                (level != null ? "level=" + level + ", " : "") +
            "}";
    }

}
