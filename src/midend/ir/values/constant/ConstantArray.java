package midend.ir.values.constant;

import midend.ir.types.ArrayType;
import midend.ir.types.Type;
import midend.ir.values.Value;

import java.util.ArrayList;

public class ConstantArray extends Constant {

    // 数组常量
    private boolean isZeroInitializer;

    public ConstantArray(ArrayType arrayType) {
        super(null, arrayType, null, new ZeroInitializer(arrayType));
        this.isZeroInitializer = true;
    }

    public ConstantArray(ArrayList<Constant> elements) {
        super(null, new ArrayType(elements.get(0).getType(), elements.size()), null, elements.toArray(new Value[elements.size()]));
        this.isZeroInitializer = false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isZeroInitializer) {
            sb.append("zeroinitializer");
        } else {
            sb.append("[");
            for (int i = 0; i < ((ArrayType) this.getType()).getElementNum(); i++) {
                sb.append(this.getUsedValue(i).getType());
                sb.append(" ");
                sb.append(this.getUsedValue(i));
                sb.append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
            sb.append("]");
        }
        return sb.toString();
    }
}
