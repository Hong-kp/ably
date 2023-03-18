package com.ably.project.customer.domain.entity

import com.ably.project.global.domain.entity.BaseEntity
import com.ably.project.global.utils.EncProc
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@Entity
@Table(name = "CUSTOMER")
@DynamicUpdate
class Customer: BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    val customerId: Long = 0

    @Column(name = "name", length = 20)
    var name: String? = null

    @Column(name = "password", length = 100)
    var password: String? = null

    @Column(name = "email", length = 100)
    var email: String? = null

    @Column(name = "mobile", length = 20)
    var mobile: String? = null

    @Column(name = "nick_name", length = 50)
    var nickName: String? = null

    /**
     * 패스워드 업데이트
     */
    fun setMyPassword(password: String){
        this.password = EncProc.getHash(password)
    }
}