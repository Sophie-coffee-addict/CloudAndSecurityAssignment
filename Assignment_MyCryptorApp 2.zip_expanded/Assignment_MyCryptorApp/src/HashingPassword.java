import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingPassword {

    public static String hashPassword(String password) {
        try {
            // 创建 MessageDigest 实例，并指定要使用的哈希算法（SHA-256）
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // 将密码转换为字节数组，并进行哈希计算
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            
            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : encodedHash) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            // 返回哈希后的密码字符串
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error hashing password: " + e.getMessage());
            return null;
        }
    }
}
