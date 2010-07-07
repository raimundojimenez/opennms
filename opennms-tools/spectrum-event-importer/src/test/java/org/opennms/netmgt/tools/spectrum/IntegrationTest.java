package org.opennms.netmgt.tools.spectrum;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opennms.test.mock.MockLogAppender;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;


public class IntegrationTest {
    private List<AlertMapping> m_alertMappings;
    private List<EventDisposition> m_eventDispositions;
    
    @Before
    public void setUp() throws IOException {
        MockLogAppender.setupLogging();
        AlertMapReader amReader = new AlertMapReader(new FileSystemResource("src/test/resources/sonus-traps/AlertMap"));
        EventDispositionReader edReader = new EventDispositionReader(new FileSystemResource("src/test/resources/sonus-traps/EventDisp"));
        m_alertMappings = amReader.getAlertMappings();
        m_eventDispositions = edReader.getEventDispositions();
    }
    
    @Test
    @Ignore
    public void integrateIpUnityInterfaceHighError() throws IOException {
        AlertMapping mapping = null;
        EventDisposition disposition = null;
        EventFormat format = null;
        
        for (AlertMapping thisMapping : m_alertMappings) {
            if (thisMapping.getEventCode().equals("0xfff002af")) {
                mapping = thisMapping;
                break;
            }
        }
        Assert.assertNotNull(mapping);
        Assert.assertEquals("Trap-OID is .1.3.6.1.4.1.5134.1.4", ".1.3.6.1.4.1.5134.1.4", mapping.getTrapOid());
        Assert.assertEquals("Trap generic-type is 0", "0", mapping.getTrapGenericType());
        Assert.assertEquals("Trap specific-type is 298", "298", mapping.getTrapSpecificType());
        
        for (EventDisposition thisDisp : m_eventDispositions) {
            if (thisDisp.getEventCode().equals("0xfff002af")) {
                disposition = thisDisp;
                break;
            }
        }
        Assert.assertNotNull(disposition);
        
        EventFormatReader efReader = new EventFormatReader(new FileSystemResource("src/test/resources/sonus-traps/CsEvFormat/Eventfff002af"));
        format = efReader.getEventFormat();
        Assert.assertNotNull(format);
    }
    
    @Test
    public void initializeImporter() throws Exception {
        SpectrumTrapImporter importer = new SpectrumTrapImporter();
        importer.setBaseUei("uei.opennms.org/import/Spectrum");
        importer.setCustomEventsDir(new UrlResource("file:src/test/resources/sonus-traps"));
        importer.afterPropertiesSet();
    }
}
