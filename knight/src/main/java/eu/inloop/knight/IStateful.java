package eu.inloop.knight;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Interface {@link IStateful}
 *
 * @author FrantisekGazo
 * @version 2015-10-20
 */
public interface IStateful {

    /**
     * Called when creating object.
     *
     * @param savedState Previous state of this object or <code>null</code>.
     */
    void onCreate(@Nullable Bundle savedState);

    /**
     * Called when saving object state.
     *
     * @param outState Bundle for saving this object state.
     */
    void onSave(@NonNull Bundle outState);

    /**
     * Called before removing object.
     */
    void onRemove();

}

