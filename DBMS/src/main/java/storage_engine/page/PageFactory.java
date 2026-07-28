package storage_engine.page;

import storage_engine.abstracts.Page;

public class PageFactory {

    public Page createPage(String pageType) {
        if ("DATA_PAGE".equalsIgnoreCase(pageType)) {
            return new DataPage();
        } else if ("INDEX_PAGE".equalsIgnoreCase(pageType)) {
            return new IndexPage();
        } else if ("CATALOG_PAGE".equalsIgnoreCase(pageType)) {
            return new CatalogPage();
        }
        return null;
    }
}
