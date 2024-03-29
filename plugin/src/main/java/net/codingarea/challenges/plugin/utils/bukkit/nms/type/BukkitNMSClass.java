package net.codingarea.challenges.plugin.utils.bukkit.nms.type;

/**
 * Used for nms classes that are represented by a bukkit class
 * @param <T> The bukkit type that represents the NMS class
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.3
 */
public abstract class BukkitNMSClass<T> extends AbstractNMSClass {

    public Object nmsObject;

    /**
     * @param nmsClass The NMS class
     */
    public BukkitNMSClass(Class<?> nmsClass, T bukkitObject) {
        super(nmsClass);
        this.nmsObject = get(bukkitObject);
    }

    /**
     * Creates an NMS object of the specified bukkit type object
     * @param bukkitObject An instance of the specified bukkit type
     * @return The NMS object
     */
    public abstract Object get(T bukkitObject);
}
