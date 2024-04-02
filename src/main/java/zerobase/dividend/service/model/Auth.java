package zerobase.dividend.service.model;

import lombok.Data;
import zerobase.dividend.service.entity.MemberEntity;

public class Auth {

    @Data
    public static class SignIn {
        private String username;
        private String password;
    }

    @Data
    public static class SignUp {
        private String username;
        private String password;
        private String role;

        public MemberEntity toEntity() {
            return MemberEntity.builder()
                    .username(username)
                    .password(password)
                    .role(role)
                    .build();
        }
    }
}
