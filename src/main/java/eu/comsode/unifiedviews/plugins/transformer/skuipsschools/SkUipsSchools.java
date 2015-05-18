package eu.comsode.unifiedviews.plugins.transformer.skuipsschools;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.unifiedviews.dataunit.DataUnit;
import eu.unifiedviews.dataunit.DataUnitException;
import eu.unifiedviews.dataunit.files.WritableFilesDataUnit;
import eu.unifiedviews.dpu.DPU;
import eu.unifiedviews.dpu.DPUException;
import eu.unifiedviews.helpers.dataunit.resource.Resource;
import eu.unifiedviews.helpers.dataunit.resource.ResourceHelpers;
import eu.unifiedviews.helpers.dataunit.virtualpath.VirtualPathHelpers;
import eu.unifiedviews.helpers.dpu.config.ConfigHistory;
import eu.unifiedviews.helpers.dpu.context.ContextUtils;
import eu.unifiedviews.helpers.dpu.exec.AbstractDpu;

/**
 * Main data processing unit class.
 */
@DPU.AsTransformer
public class SkUipsSchools extends AbstractDpu<SkUipsSchoolsConfig_V1> {

    private static final Logger LOG = LoggerFactory.getLogger(SkUipsSchools.class);

    @DataUnit.AsOutput(name = "filesOutput")
    public WritableFilesDataUnit filesOutput;

    public SkUipsSchools() {
        super(SkUipsSchoolsVaadinDialog.class, ConfigHistory.noHistory(SkUipsSchoolsConfig_V1.class));
    }

    @Override
    protected void innerExecute() throws DPUException {
        try {
            for (;;) {
                String outputVirtualPath = "aaa.xls";
                String outputSymbolicName = outputVirtualPath;
                File outputDirectory;
                outputDirectory = new File(URI.create(filesOutput.getBaseFileURIString()));
                File outputFile = File.createTempFile("____", FilenameUtils.getExtension(outputVirtualPath), outputDirectory);

                // TODO fill the output file
                // TODO do we know the mimetype of file from Content-Type HTTP header? if so, please save it as String mimetype = ...
                // TODO extract Title of file (from HTML) as String description please.
                String mimetype = "application/xls ? ";
                String description = "title from HTML page";

                filesOutput.addExistingFile(outputVirtualPath, outputFile.toURI().toASCIIString());
                VirtualPathHelpers.setVirtualPath(filesOutput, outputSymbolicName, outputVirtualPath);
                Resource resource = ResourceHelpers.getResource(filesOutput, outputSymbolicName);
                Date now = new Date();
                resource.setCreated(now);
                resource.setLast_modified(now);
                resource.setSize(outputFile.length());
                resource.setDescription(description);
                resource.setMimetype(mimetype);

                ResourceHelpers.setResource(filesOutput, outputSymbolicName, resource);
            }
        } catch (DataUnitException | IOException ex) {
            ContextUtils.dpuException(ctx, ex, "SkUipsSchools.execute.exception");
        }

    }
}
