package com.yvens.techcatalog.Entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_password_recover")
public class PasswordRecover {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false)
private String token;
@Column(nullable = false)
private String email;
@Column(nullable = false)
private Instant expiration;
public Long getId() {
    return id;
}



public PasswordRecover() {
}






public PasswordRecover(Long id, String token, String email, Instant expiration) {
    this.id = id;
    this.token = token;
    this.email = email;
    this.expiration = expiration;
}



public void setId(Long id) {
    this.id = id;
}
public String getToken() {
    return token;
}
public void setToken(String token) {
    this.token = token;
}
public String getEmail() {
    return email;
}
public void setEmail(String email) {
    this.email = email;
}
public Instant getExpiration() {
    return expiration;
}
public void setExpiration(Instant expiration) {
    this.expiration = expiration;
}
@Override
public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((token == null) ? 0 : token.hashCode());
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((expiration == null) ? 0 : expiration.hashCode());
    return result;
}
@Override
public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    PasswordRecover other = (PasswordRecover) obj;
    if (id == null) {
        if (other.id != null)
            return false;
    } else if (!id.equals(other.id))
        return false;
    if (token == null) {
        if (other.token != null)
            return false;
    } else if (!token.equals(other.token))
        return false;
    if (email == null) {
        if (other.email != null)
            return false;
    } else if (!email.equals(other.email))
        return false;
    if (expiration == null) {
        if (other.expiration != null)
            return false;
    } else if (!expiration.equals(other.expiration))
        return false;
    return true;
}


    
}
