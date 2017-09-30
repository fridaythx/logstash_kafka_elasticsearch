package com.friday.entity;

import java.util.Date;

/**
 * 消息转换实体
 * @author Friday
 *
 */
public class MessageDTO {
	private String type;
    private short severity;
    private short facility;
    private short priority;
    private Date timestamp;
    private String severityLabel;
    private String facilityLabel;
    private String host;
    private String message;

    /**
     * @param severity the severity to set
     */
    public void setSeverity(short severity) {
        this.severity = severity;
    }

    /**
     * @return the severity
     */
    public short getSeverity() {
        return severity;
    }

    /**
     * @param facility the facility to set
     */
    public void setFacility(short facility) {
        this.facility = facility;
    }

    /**
     * @return the facility
     */
    public short getFacility() {
        return facility;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(short priority) {
        this.priority = priority;
    }

    /**
     * @return the priority
     */
    public short getPriority() {
        return priority;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param severityLabel the severityLabel to set
     */
    public void setSeverityLabel(String severityLabel) {
        this.severityLabel = severityLabel;
    }

    /**
     * @return the severityLabel
     */
    public String getSeverityLabel() {
        return severityLabel;
    }

    /**
     * @param facilityLabel the facilityLabel to set
     */
    public void setFacilityLabel(String facilityLabel) {
        this.facilityLabel = facilityLabel;
    }

    /**
     * @return the facilityLabel
     */
    public String getFacilityLabel() {
        return facilityLabel;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}