```mermaid
sequenceDiagram
    autonumber

    actor User
    participant CatalogManager
    participant Database
    participant Schema

    Note over User,Schema: Catalog Metadata Lifecycle

    User->>+CatalogManager: createDatabase("SchoolDB")
    activate CatalogManager

    CatalogManager->>+CatalogManager: validateDatabaseName()
    CatalogManager->>+CatalogManager: containsDatabase()

    alt Database does not exist
        CatalogManager->>+Database: new Database("SchoolDB")
        activate Database
        Database-->>-CatalogManager: Database
        deactivate Database
    else Database already exists
        CatalogManager-->>-User: DatabaseAlreadyExistsException
    end

    User->>+CatalogManager: createSchema("public")

    CatalogManager->>+CatalogManager: getDatabase("SchoolDB")
    CatalogManager->>+Database: createSchema("public")
    activate Database

    Database->>+Database: containsSchema()

    alt Schema does not exist
        Database->>+Schema: new Schema("public")
        activate Schema
        Schema-->>-Database: Schema
        deactivate Schema
    else Schema already exists
        Database-->>-User: SchemaAlreadyExistsException
    end

    Database-->>-CatalogManager: Schema
    deactivate Database
    CatalogManager-->>-User: Success
    deactivate CatalogManager
```