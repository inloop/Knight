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

    void onCreate(@Nullable Bundle savedState);
    void onSave(@NonNull Bundle outState);
    void onRemove();

}

