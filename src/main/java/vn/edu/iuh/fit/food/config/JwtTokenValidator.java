package vn.edu.iuh.fit.food.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        if (jwt != null) {
            jwt = jwt.substring(7); // remove Bearer


            try {
                // validate the token
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

                /**
                 * 1. Jwts.parserBuilder(): Đây là phương thức bắt đầu quá trình phân tích JWT.
                 *
                 * 2. setSigningKey(key): Khóa bí mật (key) được đặt để xác minh chữ ký số của JWT. Token sẽ được ký với một khóa bí mật khi nó được tạo ra, và khi phân tích token, cần sử dụng đúng khóa này để xác minh chữ ký của nó.
                 *
                 * 3. build(): Xây dựng đối tượng JwtParser để phân tích và xác thực token.
                 *
                 * 4. parseClaimsJws(jwt): Phương thức này thực hiện quá trình phân tích token JWT.
                 * Nếu chữ ký số trong token khớp với chữ ký được tạo từ khóa bí mật (đã được đặt ở bước trước), thì token sẽ được coi là hợp lệ.
                 * Nếu chữ ký không khớp, tức là token bị giả mạo, hoặc token đã hết hạn, thì một ngoại lệ sẽ bị ném ra (ví dụ SignatureException, ExpiredJwtException, v.v.).
                 *
                 *  5. getBody(): Nếu token hợp lệ, phương thức này sẽ trả về các Claims (tức là payload chứa thông tin của token). Thông tin này có thể bao gồm email, quyền hạn, và các thông tin khác mà bạn đã mã hóa trong token khi tạo ra nó.
                 *
                 */
                Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

                String email = String.valueOf(claims.get("email"));

                String authorities = String.valueOf(claims.get("authorities"));

                System.out.println("authorities -------- " + authorities);

                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                // create an authentication object with the email and authorities of the user
                Authentication athentication = new UsernamePasswordAuthenticationToken(email, null, auths);

                SecurityContextHolder.getContext().setAuthentication(athentication);

            } catch (Exception e) {
                throw new BadCredentialsException("invalid token...");
            }
        }
        filterChain.doFilter(request, response);
    }
}
