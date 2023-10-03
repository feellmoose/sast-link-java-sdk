package sast.sastlink.sdk.model;

public class CareerRecord {
    private Integer grade;
    private String position;

    public CareerRecord() {
    }

    public CareerRecord(Integer grade, String position) {
        this.grade = grade;
        this.position = position;
    }


    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
