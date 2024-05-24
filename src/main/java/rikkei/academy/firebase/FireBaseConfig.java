package rikkei.academy.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class FireBaseConfig {
    @Bean
    public Storage storage() throws IOException {
        String serviceAccountKeyPath = "/Users/tranvanduc/Documents/Tài liệu - MacBook Pro của Trần/Module 5/Project-Module-4-HN_JV231103-Ecommerce-/src/main/resources/serviceAccountKey.json";
        InputStream serviceAccount = Files.newInputStream(Paths.get(serviceAccountKeyPath));
        return StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()
                .getService();
    }
}


