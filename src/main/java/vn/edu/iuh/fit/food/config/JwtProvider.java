package vn.edu.iuh.fit.food.config;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {

    private SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    public String generateToken(Authentication auth) {
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles = populateAuthorities(authorities);

        /**
         * Jwts.builder(): Bắt đầu quá trình tạo JWT.
         * setIssuedAt(new Date()): Đặt thời gian phát hành của token là thời điểm hiện tại.
         * setExpiration(new Date(new Date().getTime() + 86400000)): Đặt thời gian hết hạn của token là sau 24 giờ kể từ thời điểm hiện tại (86400000 ms = 24 giờ).
         * claim("email", auth.getName()): Thêm thông tin email của người dùng vào JWT thông qua một claim (payload).
         * claim("authorities", roles): Thêm danh sách quyền hạn của người dùng vào token dưới dạng một claim.
         * signWith(key): Ký token bằng khóa bí mật key (đã được cấu hình trước đó). Điều này đảm bảo tính toàn vẹn của token, giúp phát hiện việc token bị giả mạo.
         * compact(): Hoàn tất việc tạo token và trả về token dưới dạng một chuỗi (string).
         */
        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 86400000))
                .claim("email", auth.getName())
                .claim("authorities", roles)
                .signWith(key)
                .compact();
        return jwt;

    }

    public String getEmailFromJwtToken(String jwt) {
        jwt = jwt.substring(7);

        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        String email = String.valueOf(claims.get("email"));

        return email;
    }

    public String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> auths = new HashSet<>();

        for (GrantedAuthority authority : collection) {
            auths.add(authority.getAuthority());
        }
        return String.join(",", auths);
    }

}
