package com.tramp.generator.mojo;

import com.tramp.generator.Runner;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author chenjm1
 * @since 2018/11/19
 */
@Mojo(name="1-execute",defaultPhase= LifecyclePhase.PACKAGE)
public class RunnerMojo extends AbstractMojo {

    @Parameter(required = true)
    private String configPath;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Runner.run(configPath);
    }

}
