package eu.inloop.knight.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import com.sun.tools.javac.code.Symbol;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;

/**
 * Class {@link ProcessorUtils}.
 *
 * @author Frantisek Gazo
 * @version 2015-09-21
 */
public class ProcessorUtils {

    public interface IGetter<T> {
        T get(Element element);
    }

    /**
     * Discovers if {@code element} is annotated with {@code needed} annotation.
     */
    public static boolean isAnnotated(Element element, TypeName needed) {
        List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        // if given element has no annotation => end now
        if (annotationMirrors == null || annotationMirrors.size() == 0) return false;
        // go through all annotation of given element
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            // check if found annotation is the same class as needed annotation
            if (needed.equals(ClassName.get(annotationMirror.getAnnotationType().asElement().asType())))
                return true;
        }
        return false;
    }

    /**
     * Retrieves {@link ClassName} from {@code element} with {@code getter} and if exception is thrown, retrieves it from the exception.
     */
    public static ClassName getParamClass(Element element, IGetter<Class<?>> getter) {
        ClassName className;
        try {
            className = ClassName.get(getter.get(element));
        } catch (MirroredTypeException mte) {
            try {
                className = (ClassName) ClassName.get(((DeclaredType) mte.getTypeMirror()).asElement().asType());
            } catch (Exception e) { // if there is 'primitive'.class
                className = null;
            }
        }
        return className;
    }

    /**
     * Retrieves {@link ClassName}s from {@code element} with {@code getter} and if exception is thrown, retrieves it from the exception.
     */
    public static List<ClassName> getParamClasses(Element element, IGetter<Class<?>[]> getter) {
        List<ClassName> className = new ArrayList<>();
        try {
            Class<?>[] classes = getter.get(element);
            for (Class<?> cls : classes) {
                className.add(ClassName.get(cls));
            }
        } catch (MirroredTypesException mte) {
            try {
                for (TypeMirror typeMirror : mte.getTypeMirrors()) {
                    className.add((ClassName) ClassName.get(((DeclaredType) typeMirror).asElement().asType()));
                }
            } catch (Exception e) { // if there is 'primitive'.class
            }
        }
        return className;
    }

    public static boolean isSubClassOf(final TypeElement element, final ClassName cls) {
        TypeElement superClass = element;
        do {
            superClass = (TypeElement) ((Symbol.ClassSymbol) superClass).getSuperclass().asElement();
            if (superClass != null && ClassName.get(superClass).equals(cls)) {
                return true;
            }
        } while (superClass != null);
        return false;
    }

}
