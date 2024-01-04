package fun.feellmoose.enums;

/**
 * @projectName: sast-link-SDK
 * @author: feelMoose
 * @date: 2023/8/22 9:40
 */
public enum Scope {
    ALL("all");

    Scope(String name) {
        this.name = name;
    }

    public final String name;
}
