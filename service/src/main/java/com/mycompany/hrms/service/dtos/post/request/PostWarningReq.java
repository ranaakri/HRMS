package com.mycompany.hrms.service.dtos.post.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

public class PostWarningReq {
    @NotNull
    private long warnedBy;

    @NotBlank
    private String reason;

    @NotNull
    private ZonedDateTime time;

    public long getWarnedBy() {
        return warnedBy;
    }

    public void setWarnedBy(long warnedBy) {
        this.warnedBy = warnedBy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }
}
