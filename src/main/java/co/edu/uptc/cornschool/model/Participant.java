package co.edu.uptc.cornschool.model;

public class Participant {
    private String id;
    private String name;
    private Integer age;
    private Boolean gender;
    private String mail;
    private Float weight;
    private Float height;

    private Discipline discipline;

    public Participant(String id, String name, Integer age, Boolean gender, String mail, Float weight, Float height, Discipline discipline) {
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

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }
}