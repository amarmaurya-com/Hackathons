package com.code.Sales.Stock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usercred")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCred {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    @Column(nullable = false, length = 100)
    private String password;
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }
    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }
}
