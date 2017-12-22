package gr.cite.earthserver.wcps.parser.evaluation;

public class ForClauseInfo {
    public enum ForClauseType
    {
        SPECIFIC_ID_IN_SERVER,
        ALL_COVERAGES_IN_SERVER,
        ALL_COVERAGES,
        SPECIFIC_ID
    }

    private ForClauseType forType;
    private String endpoint;
    private String coverageId;

    public ForClauseType getForType() {
        return forType;
    }

    public void setForType(ForClauseType forType) {
        this.forType = forType;
    }

    public String getEndpoint() {
        return endpoint.toLowerCase();
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getCoverageId() {
        return coverageId;
    }

    public void setCoverageId(String coverageId) {
        this.coverageId = coverageId;
    }
}
