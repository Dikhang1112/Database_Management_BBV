package repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import pojo.Menu;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class MenuRepository {

    private final List<Menu> menus = new ArrayList<>();
    private final ObjectMapper objectMapper;

    public MenuRepository() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @PostConstruct
    public void loadMockData() {
        try (InputStream inputStream = new ClassPathResource("data/menus.json").getInputStream()) {
            List<Menu> mockMenus = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<Menu>>() {}
            );
            menus.clear();
            menus.addAll(mockMenus);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data/menus.json", e);
        }
    }

    // =====================================================
    // Query Operations (Flat Data)
    // =====================================================

    public List<Menu> findByParentId(Long parentId) {
        return menus.stream()
                .filter(menu -> Objects.equals(menu.getParentId(), parentId))
                .toList();
    }
}
