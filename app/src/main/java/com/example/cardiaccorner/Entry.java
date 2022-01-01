package com.example.cardiaccorner;

import java.io.Serializable;

public class Entry implements Serializable {
    private String time_created;
    private int sys_measurement;
    private int dia_measurement;
    private boolean exercise;
    private boolean sodium;
    private boolean stress;
    private String notes;
    private Boolean synced;

    public Entry(String time_created, int sys_measurement, int dia_measurement, boolean exercise, boolean sodium, boolean stress, String notes, Boolean synced) {
        this.time_created = time_created;
        this.sys_measurement = sys_measurement;
        this.dia_measurement = dia_measurement;
        this.exercise = exercise;
        this.sodium = sodium;
        this.stress = stress;
        this.notes = notes;
        this.synced = synced;
    }

    public String getTime_created() {
        return time_created;
    }

    public void setTime_created(String time_created) {
        this.time_created = time_created;
    }

    public int getSys_measurement() {
        return sys_measurement;
    }

    public void setSys_measurement(int sys_measurement) {
        this.sys_measurement = sys_measurement;
    }

    public int getDia_measurement() {
        return dia_measurement;
    }

    public void setDia_measurement(int dia_measurement) {
        this.dia_measurement = dia_measurement;
    }

    public boolean isExercise() {
        return exercise;
    }

    public void setExercise(boolean exercise) {
        this.exercise = exercise;
    }

    public boolean isSodium() {
        return sodium;
    }

    public void setSodium(boolean sodium) {
        this.sodium = sodium;
    }

    public boolean isStress() {
        return stress;
    }

    public void setStress(boolean stress) {
        this.stress = stress;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getSynced() {
        return synced;
    }

    public void setSynced(Boolean synced) {
        this.synced = synced;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "time_created='" + time_created + '\'' +
                ", sys_measurement=" + sys_measurement +
                ", dia_measurement=" + dia_measurement +
                ", exercise=" + exercise +
                ", sodium=" + sodium +
                ", stress=" + stress +
                ", notes='" + notes + '\'' +
                ", synced=" + synced +
                '}';
    }
}
