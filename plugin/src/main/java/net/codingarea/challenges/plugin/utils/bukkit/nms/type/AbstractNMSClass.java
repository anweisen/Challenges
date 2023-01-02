package net.codingarea.challenges.plugin.utils.bukkit.nms.type;

/**
 * An abstract class for other classes that represent NMS classes

 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.3
 */
public abstract class AbstractNMSClass {
    protected final Class<?> nmsClass;

    /**
     * @param nmsClass The NMS class
     */
    public AbstractNMSClass(Class<?> nmsClass) {
        this.nmsClass = nmsClass;
    }

    public Class<?> getNMSClass() {
        return nmsClass;
    }
}
