package eu.comsode.unifiedviews.plugins.transformer.skuipsschools;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

    private static final String INPUT_URL = "http://www.uips.sk/registre/zoznamy-skol-sz-v-exceli";

    @DataUnit.AsOutput(name = "filesOutput")
    public WritableFilesDataUnit filesOutput;

    public SkUipsSchools() {
        super(SkUipsSchoolsVaadinDialog.class, ConfigHistory.noHistory(SkUipsSchoolsConfig_V1.class));
    }

    @Override
    protected void innerExecute() throws DPUException {
        try {
            Document doc = null;
            doc = Jsoup.connect(INPUT_URL).userAgent("Mozilla").get();

            Element content = doc.select("td.td_content_body").first();
            Elements links = content.select("a[href]");

            for (Element fileLink : links) {
                URL website = null;
                website = new URL(fileLink.absUrl("href"));
                String outputVirtualPath = FilenameUtils.getName(website.getPath());
                String outputSymbolicName = outputVirtualPath;
                File outputDirectory;
                outputDirectory = new File(URI.create(filesOutput.getBaseFileURIString()));
                File outputFile = File.createTempFile("____", FilenameUtils.getExtension(outputVirtualPath), outputDirectory);
                FileUtils.copyURLToFile(website, outputFile);

                String description = fileLink.text();

                filesOutput.addExistingFile(outputVirtualPath, outputFile.toURI().toASCIIString());
                VirtualPathHelpers.setVirtualPath(filesOutput, outputSymbolicName, outputVirtualPath);
                Resource resource = ResourceHelpers.getResource(filesOutput, outputSymbolicName);
                Date now = new Date();
                resource.setCreated(now);
                resource.setLast_modified(now);
                resource.setSize(outputFile.length());
                resource.setDescription(description);

                ResourceHelpers.setResource(filesOutput, outputSymbolicName, resource);
            }
        } catch (DataUnitException | IOException ex) {
            throw ContextUtils.dpuException(ctx, ex, "SkUipsSchools.execute.exception");
        }

    }
}
