package midend.ir.values;

import midend.ir.types.Type;

import java.util.ArrayList;

public class User extends Value {

    private ArrayList<Value> values = new ArrayList<>();

    public User(String name, Type type, Value parent, Value... values) {
        super(name, type, parent);

        for (Value value : values) {
            this.values.add(value);
            value.addUser(this);
        }
    }

    public Value getUsedValue(int index) {
        return values.get(index);
    }

}
