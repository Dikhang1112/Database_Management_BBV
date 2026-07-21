package metadata.interfaces;

/**
 * Interface cho Composite Pattern trong metadata module.
 * Đảm bảo tất cả các phần tử trong cây CatalogManager -> Database -> Schema -> Table -> Column 
 * đều có cấu trúc tương tác đồng nhất.
 */
public interface MetadataElement {
    String getElementName();
}
