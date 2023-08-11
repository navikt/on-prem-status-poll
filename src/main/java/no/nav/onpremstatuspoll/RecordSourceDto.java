package no.nav.onpremstatuspoll;

import java.util.Arrays;
import java.util.List;

/**
* Gets or Sets RecordSource
*/
public enum RecordSourceDto {

    UNKNOWN("UNKNOWN"),
    GCP_POLL("GCP_POLL"),
    ONPREM_POLL("ONPREM_POLL"),
    PROMETHEUS("PROMETHEUS");

    private String value;

    RecordSourceDto(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static List<String> getValues() {
        return Arrays.asList(new String[] {
            "UNKNOWN",
            "GCP_POLL",
            "ONPREM_POLL",
            "PROMETHEUS",
        });
    }

    public static RecordSourceDto fromValue(String text) {
        for (RecordSourceDto b : RecordSourceDto.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + text + "' (should be one of " + getValues() + ")");
    }
}

