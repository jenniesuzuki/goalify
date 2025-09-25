package br.com.fiap.goalify.user;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(OAuth2User principal) {
        var user = userRepository.findByEmail(principal.getAttributes().get("email").toString());
        return user.orElseGet(() -> userRepository.save(new User(principal)));
    }

    public User register(OAuth2User principal, String registrationId, String accessToken) {
        Map<String, Object> attrs = principal.getAttributes();

        String email = str(attrs.get("email"));

        // Fallback só para GitHub (email privado é comum)
        if ((email == null || email.isBlank()) && isGithub(registrationId, attrs)) {
            String login = str(attrs.get("login")); // GitHub sempre manda "login"
            if (login != null && !login.isBlank()) {
                email = login + "@users.noreply.github.com";
            }
        }

        if (email == null || email.isBlank()) {
            // Se quiser, dê outra estratégia aqui; por ora, falha amigável.
            throw new IllegalStateException("Não foi possível obter um identificador de e-mail do provedor OAuth2.");
        }

        return userRepository
                .findByEmail(email)
                .orElseGet(() -> userRepository.save(new User(principal)));
    }

    private static boolean isGithub(String registrationId, Map<String, Object> attrs) {
        if (registrationId != null) {
            return "github".equalsIgnoreCase(registrationId);
        }
        // fallback por heurística (caso chamem o método sem registrationId)
        return attrs.containsKey("login") || attrs.containsKey("avatar_url");
    }

    private static String str(Object o) {
        return (o == null) ? null : o.toString();
    }
}
