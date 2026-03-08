package com.form_builder.Shared_library.jwt;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtClaimsExtractor {


        public String getUserId(Jwt jwt){
            return jwt.getClaimAsString("sub");
        }

        public List<String> getRoles(Jwt jwt){
            return jwt.getClaimAsStringList("roles");
        }


}
