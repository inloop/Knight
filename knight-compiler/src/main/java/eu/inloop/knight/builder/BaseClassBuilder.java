package eu.inloop.knight.builder;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

import eu.inloop.knight.EClass;
import eu.inloop.knight.util.ProcessorError;
import eu.inloop.knight.util.StringUtils;

/**
 * Class {@link BaseClassBuilder} is used for building a Java class file.
 *
 * @author FrantisekGazo
 * @version 2015-10-17
 */
public abstract class BaseClassBuilder {

    /**
     * Format of full class name.
     */
    private static final String FORMAT_FULL_CLASS_NAME = "%s.%s";
    /**
     * Format of class field.
     */
    private static final String FORMAT_CLASS_FIELD_NAME = "m%s";

    /**
     * These annotation classes won't be transferred to provide methods.
     */
    private static final List<ClassName> IGNORED_ANNOTATIONS = Arrays.asList(
            EClass.Override.getName(),
            // provided
            EClass.AppProvided.getName(),
            EClass.ScreenProvided.getName(),
            EClass.ActivityProvided.getName(),
            // scope
            EClass.AppScope.getName(),
            EClass.ScreenScope.getName(),
            EClass.ActivityScope.getName()
    );

    private TypeSpec.Builder mBuilder;
    private ClassName mClassName = null;
    private final GCN mGenClassName;
    private final ClassName mGenClassNameArg;
    private final GPN[] mGenPackageName;

    /**
     * Constructor
     *
     * @param isClass         <code>true</code> if generated should be <code>class</code>. <code>false</code> if <code>interface</code>.
     * @param genClassName    Name of generated class.
     * @param arg             Class name that will be used when formatting generated class name.
     * @param genPackageNames Package in which class will be generated.
     */
    public BaseClassBuilder(boolean isClass, GCN genClassName, ClassName arg, GPN... genPackageNames) throws ProcessorError {
        mGenClassName = genClassName;
        mGenClassNameArg = arg;
        mGenPackageName = genPackageNames;
        if (isClass) {
            mBuilder = TypeSpec.classBuilder(getClassName().simpleName());
        } else {
            mBuilder = TypeSpec.interfaceBuilder(getClassName().simpleName());
        }
        start();
    }

    /**
     * Constructor for <code>class</code> builder.
     *
     * @param genClassName    Name of generated class.
     * @param arg             Class name that will be used when formatting generated class name.
     * @param genPackageNames Package in which class will be generated.
     */
    public BaseClassBuilder(GCN genClassName, ClassName arg, GPN... genPackageNames) throws ProcessorError {
        this(true, genClassName, arg, genPackageNames);
    }

    /**
     * Constructor for <code>class</code> builder.
     *
     * @param genClassName    Name of generated class.
     * @param genPackageNames Package in which class will be generated.
     */
    public BaseClassBuilder(GCN genClassName, GPN... genPackageNames) throws ProcessorError {
        this(true, genClassName, null, genPackageNames);
    }

    /**
     * Constructor
     *
     * @param isClass         <code>true</code> if generated should be <code>class</code>. <code>false</code> if <code>interface</code>.
     * @param genClassName    Name of generated class.
     * @param genPackageNames Package in which class will be generated.
     */
    public BaseClassBuilder(boolean isClass, GCN genClassName, GPN... genPackageNames) throws ProcessorError {
        this(isClass, genClassName, null, genPackageNames);
    }

    /**
     * Returns {@link TypeSpec.Builder} of generated class.
     */
    public TypeSpec.Builder getBuilder() {
        return mBuilder;
    }

    protected ClassName getArgClassName() {
        return mGenClassNameArg;
    }

    /**
     * Returns {@link ClassName} of generated class.
     */
    public ClassName getClassName() {
        if (mClassName == null) {
            // build CLASS name
            String name;
            if (mGenClassNameArg == null) {
                name = mGenClassName.getmName();
            } else {
                name = String.format(mGenClassName.getmName(), mGenClassNameArg.simpleName());
            }
            // save
            mClassName = ClassName.get(GPN.toString(mGenPackageName), name);
        }
        return mClassName;
    }

    /**
     * Returns full name of generated class.
     */
    protected String getFullName() {
        return String.format(FORMAT_FULL_CLASS_NAME, getClassName().packageName(), getClassName().simpleName());
    }

    /**
     * Called in the constructor.
     */
    public void start() throws ProcessorError {
    }

    /**
     * Called before building java file.
     */
    public void end() throws ProcessorError {
    }

    /**
     * Builds Java file for generated class.
     *
     * @param filer File creator.
     */
    public void build(Filer filer) throws ProcessorError, IOException {
        end();
        TypeSpec cls = mBuilder.build();
        // create file
        JavaFile javaFile = JavaFile.builder(getClassName().packageName(), cls).build();
        javaFile.writeTo(filer);
        //javaFile.writeTo(System.out);

        System.out.println(String.format("Class <%s> successfully generated.", getFullName()));
    }

    /**
     * Returns list of all annotations given <code>element</code> has.
     *
     * @param element Element.
     */
    public List<AnnotationSpec> getAnnotations(Element element) {
        List<AnnotationSpec> list = new ArrayList<>();
        for (AnnotationMirror a : element.getAnnotationMirrors()) {
            ClassName annotationClassName = (ClassName) ClassName.get(a.getAnnotationType());
            if (!IGNORED_ANNOTATIONS.contains(annotationClassName)) {
                AnnotationSpec.Builder annotation = AnnotationSpec.builder(annotationClassName);
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : a.getElementValues().entrySet()) {
                    String format = (entry.getValue().getValue() instanceof String) ? "$S" : "$L";
                    annotation.addMember(entry.getKey().getSimpleName().toString(), format, entry.getValue().getValue());
                }
                list.add(annotation.build());
            }
        }
        return list;
    }

    /**
     * Creates class field name.
     */
    protected String createFieldName(String name) {
        return String.format(FORMAT_CLASS_FIELD_NAME, StringUtils.startUpperCase(name));
    }

}
