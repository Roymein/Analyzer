package com.example.d2j;

/**
 * represent a method_id_item in dex file format
 */
public class Method {
    /**
     * name of the method.
     */
    private final String name;
    /**
     * owner class of the method, in TypeDescriptor format.
     */
    private final String owner;
    /**
     * parameter types of the method, in TypeDescriptor format.
     */
    private final Proto proto;

    public Proto getProto() {
        return proto;
    }

    public Method(String owner, String name, String[] parameterTypes, String returnType) {
        this.owner = owner;
        this.name = name;
        this.proto = new Proto(parameterTypes, returnType);
    }

    public Method(String owner, String name, Proto proto) {
        this.owner = owner;
        this.name = name;
        this.proto = proto;
    }

    public String getDesc() {
        return proto.getDesc();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @return the parameterTypes
     */
    public String[] getParameterTypes() {
        return proto.getParameterTypes();
    }

    public String getReturnType() {
        return proto.getReturnType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Method method = (Method) o;

        if (name != null ? !name.equals(method.name) : method.name != null) return false;
        if (owner != null ? !owner.equals(method.owner) : method.owner != null) return false;
        return proto.equals(method.proto);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + proto.hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getOwner() + "." + this.getName() + this.getDesc();
    }
}
