package eu.comsode.unifiedviews.plugins.extractor.skuipsschools;

import eu.unifiedviews.dpu.config.DPUConfigException;
import eu.unifiedviews.helpers.dpu.vaadin.dialog.AbstractDialog;

/**
 * Vaadin configuration dialog .
 */
public class SkUipsSchoolsVaadinDialog extends AbstractDialog<SkUipsSchoolsConfig_V1> {

    public SkUipsSchoolsVaadinDialog() {
        super(SkUipsSchools.class);
    }

    @Override
    public void setConfiguration(SkUipsSchoolsConfig_V1 c) throws DPUConfigException {

    }

    @Override
    public SkUipsSchoolsConfig_V1 getConfiguration() throws DPUConfigException {
        final SkUipsSchoolsConfig_V1 c = new SkUipsSchoolsConfig_V1();

        return c;
    }

    @Override
    public void buildDialogLayout() {
    }

}
