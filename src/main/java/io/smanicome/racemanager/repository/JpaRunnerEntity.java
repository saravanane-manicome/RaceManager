package io.smanicome.racemanager.repository;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.PersistenceCreator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;
import java.util.UUID;

@Entity
public class JpaRunnerEntity {
    @Id
    @GeneratedValue
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;
    private String name;
    private int number;

    @ManyToOne
    private JpaRaceEntity race;

    public JpaRunnerEntity() {}

    @PersistenceCreator
    public JpaRunnerEntity(UUID id, String name, int number, JpaRaceEntity race) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.race = race;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public JpaRaceEntity getRace() {
        return race;
    }

    public void setRace(JpaRaceEntity race) {
        this.race = race;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JpaRunnerEntity that = (JpaRunnerEntity) o;
        return number == that.number && Objects.equals(id, that.id) && name.equals(that.name) && Objects.equals(race, that.race);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, number, race);
    }
}
