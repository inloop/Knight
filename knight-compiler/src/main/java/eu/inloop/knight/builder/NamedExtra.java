package eu.inloop.knight.builder;

import javax.lang.model.element.VariableElement;

import eu.inloop.knight.ErrorMsg;
import eu.inloop.knight.Extra;
import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link NamedExtra}
 *
 * @author FrantisekGazo
 * @version 2015-11-17
 */
public class NamedExtra {

    private VariableElement mElement;
    private String mName;

    public NamedExtra(VariableElement element) throws ProcessorError {
        this.mElement = element;
        String name = mElement.getAnnotation(Extra.class).value();
        this.mName = (name == null || name.isEmpty()) ? element.getSimpleName().toString() : name;
        if (!mName.replaceAll("[^a-zA-Z_0-9]", "").equals(mName)) {
            throw new ProcessorError(element, ErrorMsg.Extra_invalid_name);
        }
    }

    public VariableElement getElement() {
        return mElement;
    }

    public String getName() {
        return mName;
    }
}
