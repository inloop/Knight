package eu.inloop.knight.generator;

import com.squareup.javapoet.ClassName;

import java.io.IOException;

import javax.annotation.processing.Filer;

import eu.inloop.knight.util.ProcessorError;

/**
 * Class {@link AppScopedGenerators}
 *
 * @author FrantisekGazo
 * @version 2015-10-16
 */
public class AppScopedGenerators {

    public KnightGenerator knight;
    public ApplicationComponentGenerator AppC;
    public ApplicationModuleGenerator AppM;

    public AppScopedGenerators() throws ProcessorError {
        knight = new KnightGenerator();
        AppC = new ApplicationComponentGenerator();
        AppM = new ApplicationModuleGenerator();
    }

    public void buildAll(Filer filer) throws IOException, ProcessorError {
        knight.build(filer);
        AppC.build(filer);
        AppM.build(filer);
    }
}
