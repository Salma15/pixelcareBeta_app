package com.fdc.pixelcare.DataModel;

import java.io.Serializable;

/**
 * Created by lenovo on 14/09/2017.
 */

public class PrescriptionList implements Serializable {

    int presc_id, episode_id;
    String prod_name, generic_name, frequency, timings, duration, note, given_date;

    public PrescriptionList(String get_trade_name, String get_generic_name, String frequency, String timings,
                            String duration, String note) {
        this.prod_name = get_trade_name;
        this.generic_name = get_generic_name;
        this.frequency = frequency;
        this.timings = timings;
        this.duration = duration;
        this.note = note;
    }

    public PrescriptionList(int episode_prescription_id, int episode_id, String prescription_trade_name,
                            String prescription_generic_name, String prescription_frequency, String timing, String duration,
                            String prescription_instruction, String prescription_date_time) {
        this.presc_id = episode_prescription_id;
        this.episode_id = episode_id;
        this.prod_name = prescription_trade_name;
        this.generic_name = prescription_generic_name;
        this.frequency = prescription_frequency;
        this.timings = timing;
        this.duration = duration;
        this.note = prescription_instruction;
        this.given_date = prescription_date_time;
    }

    public int getPresID() {
        return presc_id;
    }
    public void setPrescID(int presc_id) {
        this.presc_id = presc_id;
    }

    public int getPrescEpisodeID() {
        return episode_id;
    }
    public void setPrescEpisodeID(int episode_id) {
        this.episode_id = episode_id;
    }

    public String getPrescGivenDate() {
        return given_date;
    }
    public void setPrescGivenDate(String given_date) {
        this.given_date = given_date;
    }

    public String getPrescProductName() {
        return prod_name;
    }
    public void setPrescProductName(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getPrescGenericName() {
        return generic_name;
    }
    public void setPrescGenericName(String generic_name) {
        this.generic_name = generic_name;
    }

    public String getPrescFrequency() {
        return frequency;
    }
    public void setPrescFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getPrescTimings() {
        return timings;
    }
    public void setPrescTimings(String timings) {
        this.timings = timings;
    }

    public String getPrescDuration() {
        return duration;
    }
    public void setPrescDuration(String duration) {
        this.duration = duration;
    }

    public String getPrescNote() {
        return note;
    }
    public void setPrescNote(String note) {
        this.note = note;
    }
}
