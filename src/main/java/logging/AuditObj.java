package logging;

import java.util.Date;

/**
 * Audit Object class used with GSON to
 */
public class AuditObj {

    private String content;
    private String serviceName;
    private Date timestamp;
    private String userId;

    public AuditObj() {

    }

    public AuditObj(String content, String serviceName, Date timestamp, String userId) {
        this.content = content;
        this.serviceName = serviceName;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AuditObj{" +
                "content='" + content + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", timestamp=" + timestamp +
                ", userId='" + userId + '\'' +
                '}';
    }
}
