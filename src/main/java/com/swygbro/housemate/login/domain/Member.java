package com.swygbro.housemate.login.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.util.model.AbstractEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ziphap_member")
@ToString
public class Member extends AbstractEntity implements UserDetails {

    @Id
    private String memberId;

    @Column(length = 30)
    private String memberEmail;

    private String memberName;

    private String memberProfilePicture;

    private String memberLoginRole;

    @Enumerated(STRING) @Column(name = "member_role")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "member_role", joinColumns = @JoinColumn(name = "id"))
    @Builder.Default
    private List<MemberType> memberAuthorityRoles = new ArrayList<>();

    @ManyToOne(targetEntity = Group.class, cascade = CascadeType.ALL, fetch = LAZY, optional = true)
    @JoinColumn(name = "zipHapGroupId")
    private Group zipHapGroup;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // List<Role> 형태를 Stream을 사용하여 roles 원소의 값을 String으로 바꿔주는 Enum.name()을 이용하여 List<String>형태로 변환(GrantedAuthority의 생성자는 String 타입을 받기 때문)
        List<String> rolesConvertString = this.memberAuthorityRoles.stream().map(Enum::name).collect(Collectors.toList());
        return rolesConvertString.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String getMemberRole() {
        return getAuthorities().iterator().next().toString();
    }

    @Override
    public String getPassword() {
        return "현재 애플리케이션에서는 passWord를 사용하지 않습니다.";
    }

    @Override
    public String getUsername() {
        return this.memberEmail;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }

    public void updateRole(MemberType memberType) {
        this.memberAuthorityRoles = Collections.singletonList(memberType);
    }

    public void setZipHapGroup(final Group group) {
        this.zipHapGroup = group;
    }

    public void updateName(String name) {
        this.memberName = name;
    }
}
