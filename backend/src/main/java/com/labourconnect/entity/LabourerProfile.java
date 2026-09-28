package com.labourconnect.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "labourer_profiles")
public class LabourerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String skill;
    private Integer experience;
    private Double dailyRate;
    private String location;
    private String availability;
    
    @Column(length = 1000)
    private String bio;
    
    private Double rating;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getSkill() { return skill; }
    public void setSkill(String skill) { this.skill = skill; }
    public Integer getExperience() { return experience; }
    public void setExperience(Integer experience) { this.experience = experience; }
    public Double getDailyRate() { return dailyRate; }
    public void setDailyRate(Double dailyRate) { this.dailyRate = dailyRate; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
}
