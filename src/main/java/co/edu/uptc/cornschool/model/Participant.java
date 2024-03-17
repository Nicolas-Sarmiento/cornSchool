package co.edu.uptc.cornschool.model;

import java.util.Objects;

public class Participant {
    private String id;
    private String name;
    private Integer age;
    private Boolean gender;
    private String mail;
    private double weight;
    private double height;

    private Discipline discipline;

    public Participant(String id, String name, Integer age, Boolean gender, String mail, double weight, double height, Discipline discipline) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.mail = mail;
        this.weight = weight;
        this.height = height;
        this.discipline = discipline;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Double.compare(weight, that.weight) == 0 && Double.compare(height, that.height) == 0 && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(age, that.age) && Objects.equals(gender, that.gender) && Objects.equals(mail, that.mail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, gender, mail, weight, height);
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", mail='" + mail + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", discipline=" + discipline +
                '}';
    }
}
