package com.bjut.bjut_clouddisk.utils;
import com.bjut.bjut_clouddisk.BjutCloudDiskApplication;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.ResourceUtils;

// 导入其他需要的库
import java.io.FileReader;
import java.util.HashMap;

public class TypeSuffixMapJsonReader {
    static HashMap<String, String> typeSuffixMap;
    static {
        try {
            // 读取json文件
            String diskPath = BjutCloudDiskApplication.class.getResource("").getFile();
            FileReader reader = new FileReader(diskPath+"/typeSuffixMap.json");

            // 将文件内容转换为字符串
            StringBuilder builder = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {
                builder.append((char) ch);
            }
            reader.close();

            // 将字符串转换为JSONArray
            JSONArray array = new JSONArray(builder.toString());

            // 创建一个Hashmap，key是suffix，value是type
            HashMap<String, String> map = new HashMap<>();

            // 遍历数组中的每个对象
            for (int i = 0; i < array.length(); i++) {
                // 将对象转换为JSONObject
                JSONObject jsonObject = array.getJSONObject(i);

                // 获取type和suffix的值
                String type = jsonObject.getString("type");
                JSONArray suffix = jsonObject.getJSONArray("suffix");

                // 遍历suffix数组中的每个元素
                for (int j = 0; j < suffix.length(); j++) {
                    // 将元素转换为字符串
                    String suffixStr = suffix.getString(j);

                    // 将suffix和type放入Hashmap中
                    map.put(suffixStr, type);
                }
            }
            typeSuffixMap = map;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
