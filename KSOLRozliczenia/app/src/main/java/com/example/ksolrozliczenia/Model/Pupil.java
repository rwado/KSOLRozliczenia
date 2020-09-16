package com.example.ksolrozliczenia.Model;

import java.util.Comparator;

public class Pupil   {
    public String pupilName;
    public String pupilId;

    public Pupil() {
    }

    public Pupil(String pupilName, String pupilId) {
        this.pupilName = pupilName;
        this.pupilId = pupilId;
    }

    public String getPupilName() {
        return pupilName;
    }

    public void setPupilName(String pupilName) {
        this.pupilName = pupilName;
    }

    public String getPupilId() {
        return pupilId;
    }

    public void setPupilId(String pupilId) {
        this.pupilId = pupilId;
    }



}