package in.aklabs.kv.models;

import in.aklabs.kv.core.Value;

/**
 * @param action WalAction that is being persisted
 * @param key    Value of the key that is being recorded
 * @param value  Value object that is being written / modified / updated. If action is DELETE, this will be null.
 */
public record WalRecord(WalAction action, String key, Value value) {
}
