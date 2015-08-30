package org.websure.demo.domain;

import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }, name = "UK_USERNAME")})
public class User implements UserDetails {

	private static final long serialVersionUID = -8613305487038635053L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer userId;
	
	@Email(message="error.user.username.valid")
	@NotNull(message="error.user.username.notnull")
	private String username;

	@NotNull(message="error.user.password.notnull")
	@Size(min = 4, max = 100, message="error.user.password.length")
	private String password;
	
	private boolean accountNonExpired;

	private boolean accountNonLocked;

	private boolean credentialsNonExpired;

	private boolean enabled;
	
	private Date created;

	private Date updated;

	@Transient
	private long expires;

	@Transient
	private String newPassword;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true)
	private Set<UserAuthority> authorities;

	public User() {
	}

	public User(String username) {
		this.username = username;
	}

	public User(String username, Date expires) {
		this.username = username;
		this.expires = expires.getTime();
	}

	@Override
	@JsonIgnore
	public Set<UserAuthority> getAuthorities() {

		return authorities;
	}

	public Date getCreated() {
	
		return created;
	}

	public long getExpires() {

		return expires;
	}

	public Integer getUserId() {

		return userId;
	}


	@JsonIgnore
	public String getNewPassword() {

		return newPassword;
	}


	@Override
	@JsonIgnore
	public String getPassword() {

		return password;
	}
	
	
	// Use Roles as external API
	public Set<UserRole> getRoles() {

		Set<UserRole> roles = EnumSet.noneOf(UserRole.class);
		if (authorities != null) {
			for (UserAuthority authority : authorities) {
				roles.add(UserRole.valueOf(authority));
			}
		}
		return roles;
	}

	public Date getUpdated() {
	
		return updated;
	}

	@Override
	public String getUsername() {

		return username;
	}

	public void grantRole(UserRole role) {

		if (authorities == null) {
			authorities = new HashSet<UserAuthority>();
		}
		authorities.add(role.asAuthorityFor(this));
	}

	public boolean hasRole(UserRole role) {

		return authorities.contains(role.asAuthorityFor(this));
	}

	@Override
	public boolean isAccountNonExpired() {
	
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
	
		return accountNonLocked;
	}
	@Override
	public boolean isCredentialsNonExpired() {
	
		return credentialsNonExpired;
	}
	@Override
	public boolean isEnabled() {
	
		return enabled;
	}
	@PrePersist
	public void prePersist() { // NO_UCD


		Date now = new Date();
		this.created = now;
		this.updated = now;
	}
	
	@PreUpdate
	public void preUpdate() { // NO_UCD

		this.updated = new Date();
	}

	public void revokeRole(UserRole role) {

		if (authorities != null) {
			authorities.remove(role.asAuthorityFor(this));
		}
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
	
		this.accountNonExpired = accountNonExpired;
	}

	
	public void setAccountNonLocked(boolean accountNonLocked) {
	
		this.accountNonLocked = accountNonLocked;
	}

	
	public void setAuthorities(Set<UserAuthority> authorities) {
	
		this.authorities = authorities;
	}

	
	public void setCreated(Date created) {
	
		this.created = created;
	}

	
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
	
		this.credentialsNonExpired = credentialsNonExpired;
	}

	
	public void setEnabled(boolean enabled) {
	
		this.enabled = enabled;
	}

	
	public void setExpires(long expires) {

		this.expires = expires;
	}

	
	public void setUserId(Integer userId) {

		this.userId = userId;
	}

	
	@JsonSetter
	public void setNewPassword(String newPassword) {

		this.newPassword = newPassword;
	}

	
	@JsonSetter
	public void setPassword(String password) {

		this.password = password;
	}

	
	public void setRoles(Set<UserRole> roles) {

		for (UserRole role : roles) {
			grantRole(role);
		}
	}

	
	public void setUpdated(Date updated) {
	
		this.updated = updated;
	}

	
	public void setUsername(String username) {

		this.username = username;
	}

	
	@Override
	public String toString() {

		return getClass().getSimpleName() + ": " + getUsername();
	}
	
	
}
