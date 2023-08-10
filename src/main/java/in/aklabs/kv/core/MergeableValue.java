package in.aklabs.kv.core;

/**
 * Mergeable value type. Attempt at replicating a crude CRDT type.
 * <p></p>
 * The underlying assumption of these values are that they exhibit the following properties:
 * <ul>
 * <li>Associative</li>
 * <li>Commutative</li>
 * <li>Idempotent</li>
 * </ul>
 */
public interface MergeableValue extends Value {
    /**
     * Merge the 2 instances of the Value based on custom implementation
     *
     * @param left  (previous but not always necessary) Value
     * @param right (current or new but not always necessary) Value
     * @return MergeableValue instance
     */
    MergeableValue merge(Value left, Value right);
}
