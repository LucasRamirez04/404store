package _store.cliente.service;

import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public boolean credencialesValidas(String username, String password) {
        return "admin".equals(username) && "1234".equals(password);
    }

}
