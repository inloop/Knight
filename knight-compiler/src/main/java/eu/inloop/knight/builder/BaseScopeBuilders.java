package eu.inloop.knight.builder;

import com.squareup.javapoet.ClassName;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eu.inloop.knight.ErrorMsg;
import eu.inloop.knight.util.ProcessorError;

import static eu.inloop.knight.util.ProcessorUtils.isSubClassOf;

/**
 * Class {@link BaseScopeBuilders}
 *
 * @author FrantisekGazo
 * @version 2015-11-16
 */
public abstract class BaseScopeBuilders {

    private Set<ClassName> mInjectables;

    public BaseScopeBuilders() {
        mInjectables = new HashSet<>();
    }

    public final void setInjectables(Collection<ClassName> injectables, Map<ClassName, Injectable> classesWithInject) throws ProcessorError {
        mInjectables.addAll(injectables);
        ClassName[] supported = supportedInjectableClasses();
        for (ClassName className : mInjectables) {
            Injectable injectable = classesWithInject.get(className);
            if (injectable != null) {
                if (!isSubClassOf(injectable.getElement(), supported)) {
                    throw new ProcessorError(injectable.getElement(), ErrorMsg.Invalid_injectable);
                }
                addInjectMethod(injectable);
            }
        }
    }

    public final Set<ClassName> getInjectables() {
        return mInjectables;
    }

    /**
     * Returns list of classes that are supported for injection besides Application and Activity classes.
     */
    protected abstract ClassName[] supportedInjectableClasses();

    protected abstract void addInjectMethod(Injectable injectable);

}
