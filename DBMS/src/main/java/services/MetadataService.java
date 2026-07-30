package services;

import metadata.facade.MetadataModule;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MetadataService {

    public Map<String, Object> GetInstance() {
        // Lần 1: Gọi getInstance()
        MetadataModule call1 = MetadataModule.getInstance();
        int hashCode1 = System.identityHashCode(call1);

        // Lần 2: Gọi getInstance() lần nữa
        MetadataModule call2 = MetadataModule.getInstance();
        int hashCode2 = System.identityHashCode(call2);

        // Kiểm tra xem 2 lần gọi có tham chiếu đến cùng 1 ô nhớ hay không
        boolean isSameObject = (call1 == call2);

        Map<String, Object> result = new HashMap<>();
        result.put("targetClass", "metadata.facade.MetadataModule");
        result.put("factoryMethod", "getInstance()");
        result.put("firstCallHashCode", "0x" + Integer.toHexString(hashCode1).toUpperCase());
        result.put("secondCallHashCode", "0x" + Integer.toHexString(hashCode2).toUpperCase());
        result.put("isSameInstance", isSameObject);
        result.put("singletonPatternStatus", isSameObject ? "VERIFIED (Đã xác minh Singleton duy nhất)" : "FAILED");
        result.put("hasCatalogManager", call1.getCatalogManager() != null);

        return result;
    }
}
