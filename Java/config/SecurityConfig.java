@Configuration
public class SecurityConfig {
    private static final String ALLOWED_CHARS = "^[a-zA-Z0-9_-]+$";
    private static final Pattern HTML_PATTERN = Pattern.compile("<[^>]*>");
    private static final Pattern SQL_INJECTION_PATTERN = 
        Pattern.compile("(?i)(SELECT|INSERT|UPDATE|DELETE|DROP|UNION|ALTER)");
    
    public static String sanitizeInput(String input) {
        if (input == null || !input.matches(ALLOWED_CHARS)) {
            throw new IllegalArgumentException("Invalid input");
        }
        return input;
    }
    
    // Add security headers
    public static void addSecurityHeaders(HttpGet request) {
        request.setHeader("X-Content-Type-Options", "nosniff");
        request.setHeader("X-Frame-Options", "DENY");
        request.setHeader("X-XSS-Protection", "1; mode=block");
        request.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        request.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        request.setHeader("Pragma", "no-cache");
        request.setHeader("Content-Security-Policy", 
            "default-src 'self'; " +
            "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
            "style-src 'self' 'unsafe-inline'; " +
            "img-src 'self' data: https:; " +
            "connect-src 'self' https://weibo.cn;"
        );
        request.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        request.setHeader("Permissions-Policy", "geolocation=(), microphone=(), camera=()");
    }
    
    // Add request validation
    public static void validateRequest(String url, String cookie) {
        if (url == null || !url.startsWith("https://")) {
            throw new SecurityException("Only HTTPS URLs are allowed");
        }
        if (cookie == null || cookie.contains("<script>")) {
            throw new SecurityException("Invalid cookie content");
        }
    }
    
    // Add cookie security configuration
    public static void configureCookieSecurity(HttpGet request, String cookie) {
        request.setHeader("Set-Cookie", cookie + "; Secure; HttpOnly; SameSite=Strict");
    }
    
    public static String sanitizeHtml(String input) {
        if (input == null) return "";
        return HTML_PATTERN.matcher(input).replaceAll("");
    }
    
    public static boolean containsSqlInjection(String input) {
        if (input == null) return false;
        return SQL_INJECTION_PATTERN.matcher(input).find();
    }
    
    public static String sanitizeFileName(String fileName) {
        if (fileName == null) return "";
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
    
    // Add other security-related configurations
} 