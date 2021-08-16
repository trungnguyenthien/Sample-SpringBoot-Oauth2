package asia.chiemdieng.oauth2.entity

import javax.persistence.*

open class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}



