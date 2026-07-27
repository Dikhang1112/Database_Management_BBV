# Storage Engine - Implemented Design Patterns Matrix

Tài liệu bảng ma trận đối chiếu tất cả các Design Pattern, Class, Interface, Method và công dụng thực tế trong module `storage_engine`, đồng nhất 100% với Sơ đồ lớp (Class Diagrams) trong `docs/class_diagram/storage_engine/`.

---

## 1. Core Design Patterns Matrix (By Category: Creational ➔ Structural ➔ Behavioral)

Bảng ma trận các Design Pattern cốt lõi áp dụng trong kiến trúc tổng thể module Storage Engine:

| # | Nhóm Pattern | Design Pattern | Class / Interface | Method | Công dụng (Purpose) |
|:---:|:---|:---|:---|:---|:---|
| 1 | **Creational** | **Singleton** | `BufferPoolManager` | `getInstance()` | Đảm bảo duy nhất 1 Quản lý Buffer Pool trong RAM điều phối toàn bộ bộ đệm trang và nạp đĩa. |
| 2 | **Creational** | **Factory Method** | `PageFactory` | `createPage(pageType)` | Đóng gói logic khởi tạo các loại trang dữ liệu chuyên biệt (`DataPage`, `IndexPage`, `CatalogPage`). |
| 3 | **Creational** | **Builder** | `PageBuilder` | `buildHeader()`, `buildSlots()`, `buildRecords()`, `build()` | Xây dựng từng bước cấu trúc trang Slotted-Page phức tạp từ Header, Slot Directory đến Data Records. |
| 4 | **Structural** | **Facade** | `StorageEngine` | `fetchPage(pageId)`, `allocatePage()`, `freePage()` | Cung cấp giao diện API cấp cao tập trung ẩn đi sự phức tạp tương tác giữa Buffer Pool, Page Factory và Disk File Manager. |
| 5 | **Structural** | **Adapter** | `StorageAdapter` (Interface)<br>`LocalDiskAdapter`<br>`MemoryStorageAdapter`<br>`CloudStorageAdapter` | `read(pageId)`, `write(page)`, `flush()` | Chuẩn hóa các thao tác I/O đọc/ghi dữ liệu trên nhiều môi trường lưu trữ (Đĩa cứng cục bộ, RAM, Cloud S3). |
| 6 | **Structural** | **Composite** | `BTreeNode` (Abstract Class)<br>`InternalNode`<br>`LeafNode` | `search()`, `insert()`, `split()` | Biểu diễn cấu trúc cây phân cấp chỉ mục B+Tree đồng nhất cho cả nút điều hướng và nút chứa dữ liệu lá. |
| 7 | **Behavioral** | **Strategy** | `PageReplacementStrategy` (Interface)<br>`LRUReplacementStrategy`<br>`ClockReplacementStrategy`<br>`FIFOReplacementStrategy` | `selectVictim()` | Đóng gói và thay đổi linh hoạt các thuật toán giải phóng bộ đệm (LRU, Clock, FIFO) khi Buffer Pool bị đầy. |
| 8 | **Behavioral** | **State** | `BufferFrame`<br>`BufferFrameState` (Enum) | `pin()`, `unpin()`, `markDirty()`, `setState()` | Quản lý vòng đời và các trạng thái chuyển đổi (`CLEAN`, `DIRTY`, `PINNED`, `UNPINNED`) của từng khung bộ đệm. |
| 9 | **Behavioral** | **Template Method** | `Page` (Abstract Class)<br>`DiskFileManager` | `read()`, `deserialize()`, `modify()`, `serialize()`, `write()` | Định nghĩa thuật toán khung cho luồng xử lý I/O và thao tác record trên trang dữ liệu. |
| 10 | **Behavioral** | **Iterator** | `PageIterator`<br>`BTreeIterator` | `hasNext()`, `next()` | Duyệt tuần tự dữ liệu từng dòng record trên trang và lá chỉ mục mà không làm rò rỉ cấu trúc bộ nhớ trong. |

---

## 2. Detailed Subsystem Patterns & Class Breakdown

### 2.1. Buffer Pool Management & Replacement

Bảng chi tiết các lớp thuộc phân đoạn Quản lý Bộ nhớ đệm và Thay thế Trang:

| # | Pattern / Role | Class / Interface | Method | Công dụng (Purpose) |
|:---:|:---|:---|:---|:---|
| 1 | **Singleton** | `BufferPoolManager` | `getInstance()`, `fetchPage()`, `evictPage()` | Điều phối bộ đệm trang dữ liệu trong RAM. |
| 2 | **State** | `BufferFrame` | `pin()`, `unpin()`, `markDirty()`, `setState()` | Quản lý trạng thái pin/unpin/dirty cho từng khung bộ đệm. |
| 3 | **State Enum** | `BufferFrameState` | N/A | Enum lưu trạng thái `CLEAN`, `DIRTY`, `PINNED`, `UNPINNED`. |
| 4 | **Strategy** | `PageReplacementStrategy` | `selectVictim()` | Giao diện thuật toán chọn nạn nhân giải phóng bộ đệm. |
| 5 | **Strategy Impl** | `LRUReplacementStrategy` | `selectVictim()` | Thuật toán giải phóng trang ít sử dụng nhất gần đây. |
| 6 | **Strategy Impl** | `ClockReplacementStrategy` | `selectVictim()` | Thuật toán giải phóng trang dạng Kim đồng hồ (Second-chance). |
| 7 | **Strategy Impl** | `FIFOReplacementStrategy` | `selectVictim()` | Thuật toán giải phóng trang theo thứ tự nạp vào trước. |
| 8 | **SRP Helper** | `PageTable` | `lookup()`, `insert()`, `remove()` | Bảng băm tra cứu nhanh vị trí PageID trong Buffer Frame. |
| 9 | **SRP Helper** | `FreeFrameList` | `acquireFrame()`, `releaseFrame()` | Quản lý danh sách các khung bộ đệm đang còn trống. |
| 10 | **SRP Helper** | `FlushManager` | `flushDirtyPages()` | Ghi dồn các trang bẩn (Dirty Pages) từ RAM xuống đĩa. |

---

### 2.2. Page Layout & Record Management

Bảng chi tiết các lớp thuộc phân đoạn Cấu trúc Trang và Quản lý Dòng Dữ liệu:

| # | Pattern / Role | Class / Interface | Method | Công dụng (Purpose) |
|:---:|:---|:---|:---|:---|
| 1 | **Template Method** | `Page` | `read()`, `deserialize()`, `serialize()`, `write()` | Lớp cơ sở định nghĩa khung luồng I/O cho mọi loại trang. |
| 2 | **Page Concrete** | `DataPage` | `insertRecord()`, `updateRecord()` | Trang chứa dữ liệu các dòng bản ghi (Slotted Page). |
| 3 | **Page Concrete** | `IndexPage` | `insertRecord()` | Trang chứa các khóa và con trỏ chỉ mục. |
| 4 | **Page Concrete** | `CatalogPage` | `insertRecord()` | Trang chứa thông tin hệ thống từ điển Catalog. |
| 5 | **Factory Method** | `PageFactory` | `createPage(pageType)` | Khởi tạo đối tượng trang theo đúng loại `pageType`. |
| 6 | **Builder** | `PageBuilder` | `buildHeader()`, `buildSlots()`, `buildRecords()`, `build()` | Dựng từng phần của Slotted Page hoàn chỉnh. |
| 7 | **SRP Helper** | `PageHeader` | `initialize()` | Lưu trữ Metadata tiêu đề trang (PageID, LSN, SlotCount). |
| 8 | **SRP Helper** | `SlotDirectory` | `allocateSlot()`, `freeSlot()` | Thư mục Slot quản lý offset vị trí từng bản ghi. |
| 9 | **SRP Helper** | `Record` | N/A | Đối tượng dữ liệu bản ghi thực tế. |
| 10 | **SRP Helper** | `RID` | N/A | Định danh duy nhất cho Record (PageID + SlotNo). |
| 11 | **Iterator** | `PageIterator` | `hasNext()`, `next()` | Duyệt tuần tự từng Record trên trang. |

---

### 2.3. B+Tree Index Management

Bảng chi tiết các lớp thuộc phân đoạn Chỉ mục Cây B+Tree:

| # | Pattern / Role | Class / Interface | Method | Công dụng (Purpose) |
|:---:|:---|:---|:---|:---|
| 1 | **Composite** | `BTreeNode` | `search()`, `insert()`, `split()` | Lớp trừu tượng định nghĩa nút cây B+Tree. |
| 2 | **Composite Node** | `InternalNode` | `findChild()` | Nút trong cây B+Tree chứa các khóa điều hướng. |
| 3 | **Composite Leaf** | `LeafNode` | `insertEntry()` | Nút lá cây B+Tree chứa dữ liệu/con trỏ đĩa thực tế. |
| 4 | **SRP Helper** | `KeyComparator` | `compare()` | Thẩm định và so sánh thứ tự các khóa chỉ mục. |
| 5 | **SRP Helper** | `BTreeCursor` | `seek()` | Con trỏ định vị nhanh vị trí lá trong B+Tree. |
| 6 | **Iterator** | `BTreeIterator` | `hasNext()`, `next()` | Duyệt quét dải (Range Scan) tuần tự trên các nút lá B+Tree. |

---

### 2.4. Disk File I/O & Storage Adapter

Bảng chi tiết các lớp thuộc phân đoạn Quản lý Tệp Đĩa và Adapter Lưu trữ:

| # | Pattern / Role | Class / Interface | Method | Công dụng (Purpose) |
|:---:|:---|:---|:---|:---|
| 1 | **Template Method** | `DiskFileManager` | `readPage()`, `writePage()`, `flush()` | Quản lý luồng I/O đọc/ghi tệp đĩa vật lý. |
| 2 | **Adapter Interface** | `StorageAdapter` | `read()`, `write()`, `flush()` | Giao diện chuẩn hóa các thao tác lưu trữ. |
| 3 | **Adapter Impl** | `LocalDiskAdapter` | `read()`, `write()`, `flush()` | Triển khai đọc/ghi trên tệp tin đĩa cứng HĐH. |
| 4 | **Adapter Impl** | `MemoryStorageAdapter` | `read()`, `write()`, `flush()` | Triển khai đọc/ghi giả lập trên RAM. |
| 5 | **Adapter Impl** | `CloudStorageAdapter` | `read()`, `write()`, `flush()` | Triển khai đọc/ghi trên hạ tầng đám mây S3. |
| 6 | **SRP Helper** | `PageSerializer` | `serialize(page)` | Chuyển đổi đối tượng Page thành chuỗi bytes ghi xuống đĩa. |
| 7 | **SRP Helper** | `PageDeserializer` | `deserialize(bytes)` | Chuyển đổi chuỗi bytes từ đĩa thành đối tượng Page trong RAM. |
