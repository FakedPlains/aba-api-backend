package cloud.zfwproject.abaapi.service.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/6 14:03
 */
public interface UserEnum {
    enum Role {
        ADMIN("admin", 1),
        USER("user", 0);

        private final String text;

        private final int value;

        Role(String text, int value) {
            this.text = text;
            this.value = value;
        }

        /**
         * 获取值列表
         *
         * @return
         */
        public static List<Integer> getValues() {
            return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }
}
