package com.WeGather.WeGather.models;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
public
class User extends  Users {



    @Column(unique = true)
    private String nationalNumber;
    @Column(unique = true)
    private String nationalCardNumber;
    @Column(unique = true)
    private String passportNumber;
    private String nameWrittenInPassport;


    // empty constructor
    public
    User(){ }

    // parametrized constructor
    public
    User(String userName, String password, String firstName, String middleName, String lastName, String nationalNumber, String nationalCardNumber, String passportNumber, String nameWrittenInPassport, List<String> profilePictures, Date createdAt, String authority) {
       super(userName,password,firstName,middleName,lastName,profilePictures, createdAt,authority);

       this.nationalNumber =nationalNumber;
       this.nationalCardNumber =nationalCardNumber;
       this.nameWrittenInPassport=nameWrittenInPassport;
       this.passportNumber=passportNumber;
    }

    public
    String getNationalNumber() {
        return nationalNumber;
    }

    public
    void setNationalNumber(String nationalNumber) {
        this.nationalNumber = nationalNumber;
    }

    public
    String getNationalCardNumber() {
        return nationalCardNumber;
    }

    public
    void setNationalCardNumber(String nationalCardNumber) {
        this.nationalCardNumber = nationalCardNumber;
    }

    public
    String getPassportNumber() {
        return passportNumber;
    }

    public
    void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public
    String getNameWrittenInPassport() {
        return nameWrittenInPassport;
    }

    public
    void setNameWrittenInPassport(String nameWrittenInPassport) {
        this.nameWrittenInPassport = nameWrittenInPassport;
    }
}
