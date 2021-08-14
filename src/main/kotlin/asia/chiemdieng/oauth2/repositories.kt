package asia.chiemdieng.oauth2

import asia.chiemdieng.oauth2.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findUserByUsername(username: String): User
}