package pl.estimateplus.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue (strategy= GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String userName;
    @Email
    private String email;
    @NotBlank
    private String password;
    private boolean admin;
    @OneToMany
    List<Estimate> estimates = new ArrayList<>();
    @ManyToOne
    private PriceList userPriceList;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public PriceList getUserPriceList() {
        return userPriceList;
    }

    public void setUserPriceList(PriceList userPriceList) {
        this.userPriceList = userPriceList;
    }

    public List<Estimate> getEstimates() {
        return estimates;
    }

    public void setEstimates(List<Estimate> estimates) {
        this.estimates = estimates;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", admin=" + admin +
                ", userPriceList=" + userPriceList +
                '}';
    }
}
