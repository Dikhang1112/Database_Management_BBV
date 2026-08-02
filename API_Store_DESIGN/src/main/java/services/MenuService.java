package services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pojo.Menu;
import repositories.MenuRepository;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    // =====================================================
    // Tree Building Operations
    // =====================================================

    public List<Menu> getMenuTree() {
        return menuRepository.findByParentId(null).stream()
                .sorted(Comparator.comparing(Menu::getDisplayOrder, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(rootMenu -> Menu.builder()
                        .id(rootMenu.getId())
                        .parentId(rootMenu.getParentId())
                        .title(rootMenu.getTitle())
                        .icon(rootMenu.getIcon())
                        .path(rootMenu.getPath())
                        .displayOrder(rootMenu.getDisplayOrder())
                        .type(rootMenu.getType())
                        .status(rootMenu.getStatus())
                        .children(buildChildren(rootMenu.getId()))
                        .build())
                .toList();
    }

    private List<Menu> buildChildren(Long parentId) {
        List<Menu> children = menuRepository.findByParentId(parentId).stream()
                .sorted(Comparator.comparing(Menu::getDisplayOrder, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(childMenu -> Menu.builder()
                        .id(childMenu.getId())
                        .parentId(childMenu.getParentId())
                        .title(childMenu.getTitle())
                        .icon(childMenu.getIcon())
                        .path(childMenu.getPath())
                        .displayOrder(childMenu.getDisplayOrder())
                        .type(childMenu.getType())
                        .status(childMenu.getStatus())
                        .children(buildChildren(childMenu.getId()))
                        .build())
                .toList();

        return children.isEmpty() ? null : children;
    }
}
