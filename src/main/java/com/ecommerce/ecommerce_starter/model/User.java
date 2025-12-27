// Wahi same, file ka address. Isko 'model' folder mein rakha hai.
package com.ecommerce.ecommerce_starter.model;

// Yahan security se related kuch extra cheezein import kar rahe hain.
// UserDetails Spring Security ka ek hissa hai jo user ki security details (jaise username, password, roles) ko handle karta hai.
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

// @Entity - Isse 'User' class database mein ek table ban jaayegi. Simple.
@Entity
// @Table(name="users") - By default, table ka naam 'user' hota,
// lekin 'user' databases mein ek reserved keyword ho sakta hai, isliye 'users' naam rakhna ek safe option hai.
@Table(name="users")
// 'implements UserDetails' - Yeh line super important hai!
// Iska matlab hai ki humari User class ab Spring Security ke rules follow karegi.
// Isse humein login, logout, aur permissions ( kaun kya kar sakta hai) jaise features aasani se mil jaate hain.
public class User implements UserDetails {

    @Id // Har user ki unique ID (Primary Key).
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID database khud generate karega.
    private Long id;

    // @Column - Isse hum column ki extra properties set kar sakte hain.
    // unique = true: Matlab har user ka username alag hoga. Do log "amit" username nahi rakh sakte.
    // nullable = false: Matlab username khaali (empty) nahi ho sakta. Yeh zaroori field hai.
    @Column(unique = true, nullable=false)
    private String username;
    private String password; // User ka password. Database mein isko direct save nahi karenge, encrypt karke karenge.

    // --- RELATIONSHIP WALI KAHANI ---
    // @ManyToMany: Ek user ke paas multiple roles (USER, ADMIN) ho sakte hain, aur ek role (jaise USER) multiple users ke paas ho sakta hai.
    // Isliye "Many-to-Many" relationship.
    // fetch = FetchType.EAGER: Iska matlab hai ki jab bhi user ko database se nikaaloge, uske saare roles bhi saath mein aa jaayenge.
    @ManyToMany(fetch = FetchType.EAGER)
    // @JoinTable: Jab @ManyToMany use hota hai, toh ek teesri table banti hai jo User aur Role ko jodti hai.
    // Is annotation se hum us table ko configure kar rahe hain.
    @JoinTable(name="user_roles", // Teesre table ka naam 'user_roles' hoga.
            joinColumns=@JoinColumn(name="user_id"), // Us table mein ek column 'user_id' hoga jo User table se link karega.
            inverseJoinColumns=@JoinColumn(name="role_id")) // Aur ek column 'role_id' hoga jo Role table se link karega.
    private Set<Role> roles; // 'Set' isliye use kiya taaki ek user ko same role do baar na mile.

    // --- SPRING SECURITY KE METHODS (UserDetails ke kaaran inko likhna zaroori hai) ---

    @Override
    // Yeh method batata hai ki user ke paas kya-kya permissions (roles) hain.
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Yeh thoda advanced Java hai (Streams API).
        // Simple bhasha mein: Yeh user ke 'roles' list mein se har role ka naam (e.g., "ROLE_ADMIN") nikaalta hai
        // aur usko Spring Security ki bhasha mein convert kar deta hai.
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    // Neeche ke saare methods `UserDetails` se aa rahe hain.
    // Yeh methods account ki state batate hain. Hum inko simple 'true' return kar rahe hain,
    // matlab humare saare users by default active aur enabled hain.

    @Override
    public String getPassword() { return password; } // User ka password return karta hai.

    @Override
    public String getUsername() { return username; } // User ka username return karta hai.

    @Override
    public boolean isAccountNonExpired() { return true; } // Kya account expire ho gaya hai? Nahi.

    @Override
    public boolean isAccountNonLocked() { return true; } // Kya account locked hai? Nahi.

    @Override
    public boolean isCredentialsNonExpired() { return true; } // Kya password expire ho gaya hai? Nahi.

    @Override
    public boolean isEnabled() { return true; } // Kya user enabled hai? Haan.

    // --- Constructors, Getters, and Setters ---
    // Yeh wahi same Product.java jaisa hai. Data set aur get karne ke liye.
    public User() {}
    public Long getId() { return id; }
    public Set<Role> getRoles() { return roles; }
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}