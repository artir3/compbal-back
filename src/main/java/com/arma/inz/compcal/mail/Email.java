package com.arma.inz.compcal.mail;

import com.arma.inz.compcal.users.BaseUser;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "baseUser")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "createdAt")
    private LocalDateTime createdAt;
    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @JoinTable(
            name = "base_user_email",
            joinColumns = { @JoinColumn(name = "email_id") },
            inverseJoinColumns = { @JoinColumn(name = "baseUser_id") }
    )
    private Set<BaseUser> baseUsers = new HashSet<>();
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EmailStatusEnum status;
    @Column(name = "subject")
    private String subject;
    @Column(name = "text")
    private String text;
    @Column(name = "fileName")
    private String fileName;

    public Email(BaseUser baseUser, EmailStatusEnum status, String subject, String text, String fileName) {
        this.createdAt = LocalDateTime.now();
        this.baseUsers.add(baseUser);
        this.status = status;
        this.subject = subject;
        this.text = text;
        this.fileName = fileName;
    }

    public void addBaseUser(BaseUser baseUser){
        if (this.baseUsers == null){
            this.baseUsers = new HashSet<>();
        }
        this.baseUsers.add(baseUser);
    }
}