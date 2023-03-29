package cloud.zfwproject.abaapi.service.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 帖子性别枚举
 *
 * @author yupi
 */
public interface InterfaceInfoEnum {

    enum Status {
        ONLINE("上线", 1),
        OFFLINE("下线", 0);

        private final String text;

        private final int value;

        Status(String text, int value) {
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

    enum Style {

        PATH("路径参数", 0),
        QUERY("查询参数", 1),
        BODY("请求体参数", 2),
        HEADER("请求头参数", 3),
        RETURN("返回参数", 4),
        ERROR("错误码", 5);

        private final String text;

        private final int value;

        Style(String text, int value) {
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
