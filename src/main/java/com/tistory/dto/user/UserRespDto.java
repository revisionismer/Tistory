package com.tistory.dto.user;

import com.tistory.domain.user.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class UserRespDto {

	@Getter @Setter
	@ToString
	public static class UserInfoRespDto {

		private Long id;
		private String username;
		private String email;
		private String role;
		
		public UserInfoRespDto(User userEntity) {
			this.id = userEntity.getId();
			this.username = userEntity.getUsername();
			this.email = userEntity.getEmail();
			this.role = userEntity.getRole().getValue();
		}
	}
}
