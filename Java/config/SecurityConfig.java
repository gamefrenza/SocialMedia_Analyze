@Configuration
public class SecurityConfig {
    private static final String ALLOWED_CHARS = "^[a-zA-Z0-9_-]+$";
    
    public static String sanitizeInput(String input) {
        if (input == null || !input.matches(ALLOWED_CHARS)) {
            throw new IllegalArgumentException("Invalid input");
        }
        return input;
    }
    
    // Add other security-related configurations
} 