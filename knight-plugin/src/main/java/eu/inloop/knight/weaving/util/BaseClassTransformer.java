package eu.inloop.knight.weaving.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.build.IClassTransformer;
import javassist.build.JavassistBuildException;

/**
 * Class {@link BaseClassTransformer}
 *
 * @author FrantisekGazo
 * @version 2015-11-04
 */
public abstract class BaseClassTransformer implements IClassTransformer {

    private final Set<String> mWaitingForClasses;
    private final Set<String> mStack;

    public BaseClassTransformer(Collection<String> requiredClasses) {
        this.mWaitingForClasses = new HashSet<>(requiredClasses);
        this.mStack = new HashSet<>();
    }

    @Override
    public final boolean shouldTransform(CtClass candidateClass) throws JavassistBuildException {
        // stop waiting for class if it is required
        mWaitingForClasses.remove(candidateClass.getName());
        // if all required classes were processed, then transform all stacked classes
        if (mWaitingForClasses.isEmpty() && !mStack.isEmpty()) {
            checkStackedClasses(candidateClass.getClassPool());
        }
        return canTransform(candidateClass);
    }

    private void checkStackedClasses(ClassPool classPool) throws JavassistBuildException {
        // transform previously stacked classes
        for (String className : mStack) {
            CtClass ctClass;
            try {
                ctClass = classPool.get(className);
            } catch (NotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            if (ctClass.isFrozen()) ctClass.defrost();
            transform(ctClass);
        }
        mStack.clear();
    }

    @Override
    public final void applyTransformations(CtClass classToTransform) throws JavassistBuildException {
        if (!mWaitingForClasses.isEmpty()) {
            classToTransform.stopPruning(true);
            // if not all required classes were processed, than stack this for later
            mStack.add(classToTransform.getName());
        } else {
            // transform current class
            transform(classToTransform);
        }
    }

    public abstract boolean canTransform(CtClass candidateClass) throws JavassistBuildException;

    public abstract void transform(CtClass candidateClass) throws JavassistBuildException;

}
