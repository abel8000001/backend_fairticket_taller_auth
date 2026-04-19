package upb.edu.co.fairticket.domain.port;

public interface CryptoHasher {
    String hash(String rawValue);
    boolean matches(String rawValue, String hashedValue);
}
