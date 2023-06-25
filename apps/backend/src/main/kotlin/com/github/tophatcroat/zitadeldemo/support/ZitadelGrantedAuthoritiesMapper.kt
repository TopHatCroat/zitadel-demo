package com.github.tophatcroat.zitadeldemo.support

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority
import java.util.function.Consumer

class ZitadelGrantedAuthoritiesMapper : GrantedAuthoritiesMapper {
    override fun mapAuthorities(authorities: Collection<GrantedAuthority>): Collection<GrantedAuthority> {
        val mappedAuthorities = HashSet<GrantedAuthority>()
        authorities.forEach { authority: GrantedAuthority? ->
            if (authority is SimpleGrantedAuthority) {
                mappedAuthorities.add(authority)
            }
            if (authority is OidcUserAuthority) {
                addRolesFromUserInfo(mappedAuthorities, authority)
            }
        }
        return mappedAuthorities
    }

    private fun addRolesFromUserInfo(
        mappedAuthorities: HashSet<GrantedAuthority>,
        oidcUserAuthority: OidcUserAuthority
    ) {
        val userInfo = oidcUserAuthority.userInfo
        val roleInfo = userInfo.getClaimAsMap(ZITADEL_ROLES_CLAIM)
        if (roleInfo == null || roleInfo.isEmpty()) {
            return
        }
        roleInfo.keys.forEach(Consumer { zitadelRoleName: String ->
            mappedAuthorities.add(
                SimpleGrantedAuthority("ROLE_$zitadelRoleName")
            )
        })
    }

    companion object {
        const val ZITADEL_ROLES_CLAIM = "urn:zitadel:iam:org:project:roles"
    }
}