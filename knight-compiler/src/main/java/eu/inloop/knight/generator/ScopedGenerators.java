package eu.inloop.knight.generator;

import com.squareup.javapoet.ClassName;

import java.io.IOException;

import javax.annotation.processing.Filer;

import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link ScopedGenerators}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class ScopedGenerators {

    public ClassName className;

    public ScreenComponentGenerator SC;
    public ActivityComponentGenerator AC;
    public ScreenModuleGenerator SM;
    public ActivityModuleGenerator AM;

    public ScopedGenerators(ClassName className) throws ProcessorError {
        this.className = className;

        SM = new ScreenModuleGenerator(className);
        SC = new ScreenComponentGenerator(className);
        AM = new ActivityModuleGenerator(className);
        AC = new ActivityComponentGenerator(className);
    }

    public void buildAll(Filer filer) throws IOException, ProcessorError {
        SM.build(filer);
        SC.build(filer);
        AM.build(filer);
        AC.build(filer);
    }
}
