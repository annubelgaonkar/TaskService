package dev.anuradha.taskservice.service;

//import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import dev.anuradha.taskservice.model.User;
import dev.anuradha.taskservice.repository.UserRepository;
@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
    // Get user by ID (for JWT authentication)
    public User getUserById(Long userId) {
        return userRepository.findById(userId).
                orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }
}
