package eu.inloop.knight.weaving;

import javassist.CtClass;
import javassist.build.IClassTransformer;
import javassist.build.JavassistBuildException;

/**
 * Class {@link ANoOpClassTransformer}
 *
 * @author FrantisekGazo
 * @version 2015-11-05
 */
public class ANoOpClassTransformer implements IClassTransformer {
    @Override
    public void applyTransformations(CtClass ctClass) throws JavassistBuildException {

    }

    @Override
    public boolean shouldTransform(CtClass ctClass) throws JavassistBuildException {
        log("%s", ctClass.getName());
        //log("POOL %s", ctClass.getClassPool());
        return false;
    }

    private void log(String msg, Object... args) {
        System.out.printf("@ NoOp : " + msg + "\n", args);
    }

}
