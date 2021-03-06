package com.example.cardiaccorner;

import java.io.Serializable;
import java.util.Objects;

/**
 * The entry class is a class which stores the values belonging to a log
 * it implements serializable because the arraylist that it is stored into
 * will be serialized at one point.
 *
 * @Authors: David Casciano and Laura Reid
 */
public class Entry implements Serializable {

    //Private data variables
    private String time_created;
    private int sys_measurement;
    private int dia_measurement;
    private boolean exercise;
    private boolean sodium;
    private boolean stress;
    private String notes;
    private Boolean synced;


    /**
     *
     * @param time_created time that the entry was captures (String)
     * @param sys_measurement Systolic Measurement (int)
     * @param dia_measurement Diastolic Measurement (int)
     * @param exercise Heavy Exercise Flag (Boolean)
     * @param sodium High Sodium Flag (Boolean)
     * @param stress High Stress Flag (Boolean)
     * @param notes Additional Notes flag (String)
     * @param synced backend synced flag (Boolean)
     */
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

    /**
     *
     * As all of these are private variables autogenerated getter and setters have been made
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entry)) return false;
        Entry entry = (Entry) o;
        return getSys_measurement() == entry.getSys_measurement() && getDia_measurement() == entry.getDia_measurement() && isExercise() == entry.isExercise() && isSodium() == entry.isSodium() && isStress() == entry.isStress() && getTime_created().equals(entry.getTime_created()) && getNotes().equals(entry.getNotes()) && getSynced().equals(entry.getSynced());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTime_created(), getSys_measurement(), getDia_measurement(), isExercise(), isSodium(), isStress(), getNotes(), getSynced());
    }

    /**
     * This is a helper function which will return the proper newline separated list for
     * the graph tags
     * @return a newline separated list in string format
     */
    public String getTags() {

        String tags = "";

        if (sodium){
            tags = "sodium";

            if (stress){
                tags = tags + "\n" + "stress" ;
            }
            if (exercise){
                tags = tags + "\n" + "exercise" ;
            }
        }
        if (!sodium){
            if (stress){
                tags = "stress" ;

                if (exercise){
                    tags = tags + "\n" + "exercise" ;
                }
            } else {
                if (exercise){
                    tags = "exercise" ;
                }
            }
        }


        return tags;
    }

}
