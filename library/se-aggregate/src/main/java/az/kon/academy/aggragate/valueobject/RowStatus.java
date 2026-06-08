package az.kon.academy.aggragate.valueobject;

/**
 * Represents the soft-delete lifecycle status of an aggregate.
 * <ul>
 *   <li>{@link #ACTIVE} — visible and operational</li>
 *   <li>{@link #HIDDEN} — temporarily concealed</li>
 *   <li>{@link #ARCHIVED} — preserved but not active</li>
 *   <li>{@link #DELETED} — marked for removal</li>
 * </ul>
 */
public enum RowStatus {
    ACTIVE,
    HIDDEN,
    ARCHIVED,
    DELETED;
}
