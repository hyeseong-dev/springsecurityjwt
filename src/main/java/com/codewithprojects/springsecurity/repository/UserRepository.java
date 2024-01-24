package com.codewithprojects.springsecurity.repository;

import com.codewithprojects.springsecurity.entities.Role;
import com.codewithprojects.springsecurity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository 인터페이스는 사용자(User) 엔티티에 대한 데이터 접근을 관리합니다.
 * 이 인터페이스는 JpaRepository를 상속받아 CRUD(Create, Read, Update, Delete) 기능과
 * 페이징 및 정렬 기능을 제공합니다.
 *
 * @Repository 애노테이션은 이 인터페이스가 데이터 접근 계층의 컴포넌트임을 나타냅니다.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 주어진 이메일 주소로 사용자를 찾습니다.
     * 이메일 주소에 해당하는 사용자가 존재하지 않을 경우, 빈 Optional을 반환합니다.
     *
     * @param email 찾고자 하는 사용자의 이메일 주소
     * @return 해당 이메일 주소를 가진 사용자의 Optional 객체
     */
    Optional<User> findByEmail(String email);

    /**
     * 주어진 역할(Role)에 해당하는 사용자를 찾습니다.
     * 이 메서드는 특정 역할을 가진 첫 번째 사용자를 반환합니다.
     * 해당 역할을 가진 사용자가 존재하지 않을 경우, null을 반환할 수 있습니다.
     *
     * @param role 찾고자 하는 사용자의 역할
     * @return 해당 역할을 가진 사용자 객체, 또는 사용자가 존재하지 않을 경우 null
     */
    User findByRole(Role role);
}