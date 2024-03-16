package co.edu.uptc.cornschool.model;

import java.util.Objects;

public class Discipline {
    private String id;
    private String name;
    private String description;
    private Boolean inGroup;

    public Discipline(String id, String name, String description, Boolean inGroup) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.inGroup = inGroup;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getInGroup() {
        return inGroup;
    }

    public void setInGroup(Boolean inGroup) {
        this.inGroup = inGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discipline that = (Discipline) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(inGroup, that.inGroup);
    }

    @Override
    public int hashCode() {
        String hashName = name.replaceAll(" ","").toLowerCase();
        String hashDescription = description.replaceAll(" ","").toLowerCase();
        return Objects.hash(hashName, hashDescription, inGroup);
    }

    @Override
    public String toString() {
        return "Discipline{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", inGroup=" + inGroup +
                '}';
    }
}
