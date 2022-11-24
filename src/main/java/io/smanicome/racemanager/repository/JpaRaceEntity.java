package io.smanicome.racemanager.repository;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.PersistenceCreator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class JpaRaceEntity {
    @Id
    @GeneratedValue
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;
    private LocalDateTime date;
    private int number;
    @OneToMany
    private List<JpaRunnerEntity> runners;

    public JpaRaceEntity() {

    }

    @PersistenceCreator
    public JpaRaceEntity(UUID id, LocalDateTime date, int number, List<JpaRunnerEntity> runners) {
        this.id = id;
        this.date = date;
        this.number = number;
        this.runners = runners;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<JpaRunnerEntity> getRunners() {
        return runners;
    }

    public void setRunners(List<JpaRunnerEntity> runners) {
        this.runners = runners;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JpaRaceEntity that = (JpaRaceEntity) o;
        return number == that.number && Objects.equals(id, that.id) && date.equals(that.date) && runners.equals(that.runners);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, number, runners);
    }
}
