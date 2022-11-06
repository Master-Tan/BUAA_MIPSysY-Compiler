package midend.ir.values;

import midend.ir.types.Type;

import java.util.ArrayList;

public class Value {

    private static int idCounter = 0;

    private ArrayList<User> users = new ArrayList<>();

    private int id;
    private String name;
    private Type type;
    private Value parent;

    public Value(String name, Type type, Value parent) {
        this.id = idCounter;
        idCounter++;
        this.name = name;
        this.type = type;
        this.parent = parent;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Value getParent() {
        return parent;
    }
}
