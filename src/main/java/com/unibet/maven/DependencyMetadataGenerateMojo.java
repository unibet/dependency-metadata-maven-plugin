package com.unibet.maven;

import com.unibet.maven.domain.Metadata;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProjectHelper;

import java.io.File;
import java.io.IOException;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = false)
public class DependencyMetadataGenerateMojo extends AbstractDependencyMetadataMojo {

    @Parameter(property = "dependency.metadata.message", defaultValue = "Artifact has been deprecated!" +
            " Please consider updating the version!")
    private String message;

    @Parameter(property = "dependency.metadata.fail", defaultValue = "false")
    private boolean fail;

    @Parameter(property = "dependency.metadata.applyOnPreviousVersions", defaultValue = "false")
    private boolean applyOnPreviousVersions;

    @Component
    private MavenProjectHelper projectHelper;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Metadata metadata = new Metadata();
        metadata.formatVersion = this.formatVersion;
        metadata.message = this.message;
        metadata.fail = this.fail;
        metadata.applyOnPreviousVersions = this.applyOnPreviousVersions;
        // Make sure build directory exists
        new File(project.getBuild().getDirectory()).mkdirs();
        try {
            OBJECT_MAPPER.writeValue(getArtifactFile(), metadata);
        } catch (IOException e) {
            throw new MojoFailureException("Failed creating metadata artifact file " + getArtifactFile(), e);
        }

        logger.info("Metadata artifact generated: {}", getArtifactFile());
    }
}