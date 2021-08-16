package asia.chiemdieng.oauth2

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/auth")
class AuthController(
    @Autowired tokenService: TokenService,
    @Autowired userDetailsService: UserDetailsService
) {
    @PostMapping("/signUp")
    fun signUp() {

    }

    @PostMapping("/signIn")
    fun signIn() {

    }

    @PostMapping("/refresh")
    fun refreshToken() {

    }
}

data class SignUpRequest(
    val username: String,
    val password: String,
    val email: String?
)

data class RefreshTokenRequest(
    val refreshToken: String
)

data class SignInRequest(
    val username: String,
    val password: String
)

data class TokenResponse(
    val refreshToken: String,
    val accessToken: String,
    val type: String,
    val expireIn: Long
)