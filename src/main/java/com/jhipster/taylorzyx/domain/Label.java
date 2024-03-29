package com.jhipster.taylorzyx.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Label.
 */
@Table("label")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Label implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Size(min = 3)
    @Column("label")
    private String label;

    @Size(min = 5)
    @Column("jhi_desc")
    private String desc;

    @Column("fake_number")
    private Integer fakeNumber;

    @Transient
    @JsonIgnoreProperties(value = { "project", "assignedTo", "labels" }, allowSetters = true)
    private Set<Ticket> tickets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Label id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public Label label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDesc() {
        return this.desc;
    }

    public Label desc(String desc) {
        this.setDesc(desc);
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getFakeNumber() {
        return this.fakeNumber;
    }

    public Label fakeNumber(Integer fakeNumber) {
        this.setFakeNumber(fakeNumber);
        return this;
    }

    public void setFakeNumber(Integer fakeNumber) {
        this.fakeNumber = fakeNumber;
    }

    public Set<Ticket> getTickets() {
        return this.tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        if (this.tickets != null) {
            this.tickets.forEach(i -> i.removeLabel(this));
        }
        if (tickets != null) {
            tickets.forEach(i -> i.addLabel(this));
        }
        this.tickets = tickets;
    }

    public Label tickets(Set<Ticket> tickets) {
        this.setTickets(tickets);
        return this;
    }

    public Label addTicket(Ticket ticket) {
        this.tickets.add(ticket);
        ticket.getLabels().add(this);
        return this;
    }

    public Label removeTicket(Ticket ticket) {
        this.tickets.remove(ticket);
        ticket.getLabels().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Label)) {
            return false;
        }
        return getId() != null && getId().equals(((Label) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Label{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", desc='" + getDesc() + "'" +
            ", fakeNumber=" + getFakeNumber() +
            "}";
    }
}
