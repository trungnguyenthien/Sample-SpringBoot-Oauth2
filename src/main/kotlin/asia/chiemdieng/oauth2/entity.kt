package asia.chiemdieng.oauth2.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

open class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}

@Entity class User: BaseEntity(), UserDetails {
    private val username: String? = null
    private val password: String? = null
    private val enabled = false
    private var accountLocked = false
    private var accountExpired = false
    private var credentialsExpired = false

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "role_user",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    private val roles: List<Role> = emptyList()

    override fun getAuthorities() = grantedAuthority(roles)
    override fun getPassword() = password
    override fun getUsername() = username
    override fun isAccountNonExpired() = !accountExpired
    override fun isAccountNonLocked() = !accountLocked
    override fun isCredentialsNonExpired() = !credentialsExpired
    override fun isEnabled() = enabled
}

@Entity class Role: BaseEntity() {
    val name: String? = null

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "permission_role",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "permission_id", referencedColumnName = "id")]
    )
    val permissions: List<Permission> = emptyList()
}

@Entity class Permission: BaseEntity() {
    val name: String? = null
}

private fun grantedAuthority(roles: List<Role>): MutableCollection<out GrantedAuthority> {
    var names: ArrayList<String> = ArrayList()
    val roleNames = roles.map { it.name }.filterNotNull()
    val permissionNames = roles.flatMap { it.permissions.map { it.name } }.filterNotNull()
    names.addAll(roleNames)
    names.addAll(permissionNames)
    val authorities: MutableSet<GrantedAuthority> = HashSet()
    names.distinct().forEach { authorities.add(SimpleGrantedAuthority(it)) }
    return authorities
}

