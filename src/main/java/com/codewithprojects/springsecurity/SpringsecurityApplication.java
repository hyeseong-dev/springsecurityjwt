package com.codewithprojects.springsecurity;

import com.codewithprojects.springsecurity.entities.Role;
import com.codewithprojects.springsecurity.entities.User;
import com.codewithprojects.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * SpringsecurityApplication 클래스는 스프링 부트 애플리케이션의 주 진입점입니다.
 * CommandLineRunner 인터페이스를 구현하여 애플리케이션이 시작될 때 실행되는 로직을 포함합니다.
 */
@SpringBootApplication
public class SpringsecurityApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringsecurityApplication.class, args);
	}

	/**
	 * 애플리케이션 시작 시 실행되는 메서드입니다.
	 * 관리자 계정이 데이터베이스에 존재하지 않는 경우 새로운 관리자 계정을 생성하고 저장합니다.
	 *
	 * @param args 커맨드 라인에서 제공된 인자들
	 */
	public void run(String... args){
		User adminAccount = userRepository.findByRole(Role.ADMIN);
		if(null == adminAccount){
			User userEntity = new User();
			userEntity.setEmail("admin1@gmail.com");
			userEntity.setFirstname("adminFirstname");
			userEntity.setSecondname("adminSecondname");
			userEntity.setRole(Role.ADMIN);
			userEntity.setPassword(new BCryptPasswordEncoder().encode("admin"));
			userRepository.save(userEntity);
		}
	}
}
