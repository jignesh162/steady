package com.steady.nifty.strategy.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "steady", name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username") })
@Getter
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    protected User() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }

    /**
     * Constructor for new user
     * 
     * @param username
     *                        username
     * @param password
     *                        encrypted password
     * @param enabled
     *                        true = enabled
     * @param authorities
     *                        Set of roles
     */
    public User(String username, String password, boolean enabled, Set<Authority> authorities) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.setAuthorities(authorities);
    }

    /**
     * Set payload for updating user
     * 
     * @param enabled
     *                        true = enabled
     * @param password
     *                        encrypted password, if null - the password won't be updated
     * @param authorities
     *                        Set of roles
     */
    public void setPayload(boolean enabled, String password, Set<Authority> authorities) {
        this.enabled = enabled;
        if (!StringUtils.isEmpty(password)) {
            this.password = password;
        }
        this.setAuthorities(authorities);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String username;

    @NotBlank
    @Size(max = 200)
    @Column(length = 200, nullable = false)
    private String password;

    @NotNull
    private boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = "steady", name = "users_authorities", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "authority_id"))
    @EqualsAndHashCode.Exclude
    @Setter
    private Set<Authority> authorities;

    @Schema(accessMode = AccessMode.READ_ONLY)
    @Column(nullable = false)
    @LastModifiedDate
    @JsonView(Views.Default.class)
    private LocalDateTime modified;

    @Schema(accessMode = AccessMode.READ_ONLY, example = "userid")
    @LastModifiedBy
    @Column(length = 50, nullable = false)
    @JsonView(Views.Default.class)
    private String modifiedBy;

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }
}
