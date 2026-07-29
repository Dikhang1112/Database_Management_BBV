package storage_engine.page;

import storage_engine.abstracts.Page;

public class PageFactory {

    public Page createPage(PageType pageType) {
        if (pageType == null) {
            throw new IllegalArgumentException("PageType cannot be null");
        }
        return switch (pageType) {
            case DATA_PAGE -> new DataPage();
            case INDEX_PAGE -> new IndexPage();
            case CATALOG_PAGE -> new CatalogPage();
        };
    }
}
