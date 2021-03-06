package esg.search.publish.thredds.parsers;

import org.springframework.util.StringUtils;

import thredds.catalog.InvDataset;
import thredds.catalog.ThreddsMetadata.GeospatialCoverage;
import esg.search.core.Record;
import esg.search.query.impl.solr.SolrXmlPars;

/**
 * Class that parsers a THREDDS <geospatialCoverage> element.
 * 
 * <geospatialCoverage zpositive="down">
 *      <northsouth>
 *          <start>36.6058</start>
 *          <size>0.0</size>
 *          <units>degrees_north</units>
 *      </northsouth>
 *      <eastwest>
 *          <start>-97.4888</start>
 *          <size>0.0</size>
 *          <units>degrees_west</units>
 *      </eastwest>
 *      <updown>
 *          <start>314.0</start>
 *          <size>0.0</size>
 *          <units>m</units>
 *      </updown>
 *  </geospatialCoverage>
 *
 * @author Luca Cinquini
 *
 */
public class GeospatialCoverageParser implements ThreddsElementParser {

    @Override
    public void parse(InvDataset dataset, Record record, final DatasetSummary ds) {
       
        final GeospatialCoverage gsc = dataset.getGeospatialCoverage();
        
        if (gsc!=null) {
            
            // record metadata
            if (gsc.getNorthSouthRange()!=null)
                record.addField(SolrXmlPars.FIELD_SOUTH, Double.toString(gsc.getNorthSouthRange().getStart()));
            if (gsc.getNorthSouthRange()!=null)
                record.addField(SolrXmlPars.FIELD_NORTH, Double.toString(gsc.getNorthSouthRange().getStart()+gsc.getNorthSouthRange().getSize()));
            if (gsc.getEastWestRange()!=null)
                record.addField(SolrXmlPars.FIELD_WEST, Double.toString(gsc.getEastWestRange().getStart()));
            if (gsc.getEastWestRange()!=null)
                record.addField(SolrXmlPars.FIELD_EAST, Double.toString(gsc.getEastWestRange().getStart()+gsc.getEastWestRange().getSize()));
            if (gsc.getUpDownRange()!=null) {
                record.addField(SolrXmlPars.FIELD_HEIGHT_BOTTOM, Double.toString(gsc.getUpDownRange().getStart()));
                record.addField(SolrXmlPars.FIELD_HEIGHT_TOP, Double.toString(gsc.getUpDownRange().getStart()+gsc.getUpDownRange().getSize()));
                record.addField(SolrXmlPars.FIELD_HEIGHT_UNITS, gsc.getHeightUnits());
            }
            
            // summary metadata
            if (gsc.getNorthSouthRange()!=null) {        
                if (ds.latNorth<gsc.getLatNorth()) ds.latNorth = gsc.getLatNorth();
                if (ds.latSouth>gsc.getLatSouth()) ds.latSouth = gsc.getLatSouth();                
            }
            if (gsc.getEastWestRange()!=null) {                
                if (ds.lonEast<gsc.getLonEast()) ds.lonEast = gsc.getLonEast();
                if (ds.lonWest>gsc.getLonWest()) ds.lonWest = gsc.getLonWest();                
            }
            if (gsc.getUpDownRange()!=null) {
                if (ds.heightBottom>gsc.getUpDownRange().getStart()) ds.heightBottom=gsc.getUpDownRange().getStart();
                if (ds.heightTop<(gsc.getUpDownRange().getStart()+gsc.getUpDownRange().getSize())) 
                    ds.heightTop=(gsc.getUpDownRange().getStart()+gsc.getUpDownRange().getSize());
                if (StringUtils.hasText(gsc.getHeightUnits())) ds.heightUnits = gsc.getHeightUnits();
            }
            
        }

    }
}