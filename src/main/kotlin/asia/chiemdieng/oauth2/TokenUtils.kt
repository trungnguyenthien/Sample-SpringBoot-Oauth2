package asia.chiemdieng.oauth2

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import io.jsonwebtoken.Jwts

import io.jsonwebtoken.Claims
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import java.util.HashMap





@Component
class TokenUtils {
    @Value("\${jwt.secret}")
    private val secret: String = ""

    val JWT_TOKEN_VALIDITY = (5 * 60 * 60).toLong()

    fun getUsernameFromToken(token: String) = getAllClaimsFromToken(token).subject
    fun getExpirationDateFromToken(token: String) = getAllClaimsFromToken(token).expiration

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    fun generateToken(userDetails: UserDetails): String? {
        val claims: Map<String, Any> = HashMap()
        return doGenerateToken(claims, userDetails.username)
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private fun doGenerateToken(claims: Map<String, Any>, subject: String) = Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact()

    private fun isTokenExpired(token: String) = getExpirationDateFromToken(token).before(Date())

    private fun getAllClaimsFromToken(token: String) = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body

}