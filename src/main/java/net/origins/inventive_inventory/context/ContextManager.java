package net.origins.inventive_inventory.context;

public class ContextManager {
    private static Contexts CONTEXT = Contexts.INIT;

    public static void setContext(Contexts context) {
        CONTEXT = context;
    }

    public static boolean isInit() {
        return CONTEXT == Contexts.INIT;
    }
}
