package cloud.zfwproject.abaapi.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author 46029
 * @version 1.0
 * @description 枚举注解校验
 * @date 2023/4/9 16:03
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, Integer> {

    private int[] values;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        this.values = constraintAnnotation.values();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        for (int i : values) {
            if (value == i) {
                return true;
            }
        }
        return false;
    }

}
