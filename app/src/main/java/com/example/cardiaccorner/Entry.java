package com.example.cardiaccorner;

public class Entry {
    private String time_created;
    private int sys_measurement;
    private int dia_measurement;
    private boolean heavy_exer;
    private boolean sodium;
    private boolean stress;
    private String notes;

    public Entry(String time_created, int sys_measurement, int dia_measurement, boolean heavy_exer, boolean sodium, boolean stress, String notes) {
        this.time_created = time_created;
        this.sys_measurement = sys_measurement;
        this.dia_measurement = dia_measurement;
        this.heavy_exer = heavy_exer;
        this.sodium = sodium;
        this.stress = stress;
        this.notes = notes;
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

    public boolean isHeavy_exer() {
        return heavy_exer;
    }

    public void setHeavy_exer(boolean heavy_exer) {
        this.heavy_exer = heavy_exer;
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

    @Override
    public String toString() {
        return "Entry{" +
                "time_created='" + time_created + '\'' +
                ", sys_measurement=" + sys_measurement +
                ", dia_measurement=" + dia_measurement +
                ", heavy_exer=" + heavy_exer +
                ", sodium=" + sodium +
                ", stress=" + stress +
                ", notes='" + notes + '\'' +
                '}';
    }
}
