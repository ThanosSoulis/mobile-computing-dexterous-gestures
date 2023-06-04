package com.example.dexterous_gestures;

public class UserStudyModel {
    String userId;
    String contextId;
    String expectedGesture;
    String actualGesture;
    String responseTime;
    String timeStampAlarmStart;
    String TimeStampAlarmSop;

    public String getUserId() {
        return userId;
    }

    public String getActualGesture() {
        return actualGesture;
    }

    public void setActualGesture(String actualGesture) {
        this.actualGesture = actualGesture;
    }

    public String getTimeStampAlarmStart() {
        return timeStampAlarmStart;
    }

    public void setTimeStampAlarmStart(String timeStampAlarmStart) {
        this.timeStampAlarmStart = timeStampAlarmStart;
    }

    public String getTimeStampAlarmSop() {
        return TimeStampAlarmSop;
    }

    public void setTimeStampAlarmSop(String timeStampAlarmSop) {
        TimeStampAlarmSop = timeStampAlarmSop;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public String getExpectedGesture() {
        return expectedGesture;
    }

    public void setExpectedGesture(String expectedGesture) {
        this.expectedGesture = expectedGesture;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }
}
