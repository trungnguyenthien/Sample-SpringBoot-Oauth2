package asia.chiemdieng.oauth2

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserDetailServiceImpl(@Autowired val repo: UserRepository): UserDetailsService {
    override fun loadUserByUsername(username: String?) = repo.findUserByUsername(username ?: "")
}

@Service
class TokenService(
    @Value("\${jwt.secret}") val secret: String,
    @Value("\${jwt.tokenExpireByHour}") val tokenExpireByHour: Int,
    @Value("\${jwt.refreshtokenExpireByHour}") val refreshtokenExpireByHour: Int
) {

    fun usernameFromToken(token: String) = claimsFromToken(token).subject
    fun expirationDateFromToken(token: String) = claimsFromToken(token).expiration

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = usernameFromToken(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    fun generateToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = HashMap()
        return doGenerateToken(claims, userDetails.username)
    }

    private fun jwtTokenValidityByMs() = (tokenExpireByHour * 60 * 60 * 1000).toLong()
    private fun isTokenExpired(token: String) = expirationDateFromToken(token).before(Date())

    private fun doGenerateToken(claims: Map<String, Any>, username: String) = Jwts.builder()
        .setClaims(claims)
        .setSubject(username)
        .setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(Date(System.currentTimeMillis() + jwtTokenValidityByMs()))
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact()



    private fun claimsFromToken(token: String) = Jwts.parser()
        .setSigningKey(secret)
        .parseClaimsJws(token)
        .body
}