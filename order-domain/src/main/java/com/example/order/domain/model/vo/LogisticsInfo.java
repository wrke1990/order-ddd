package com.example.order.domain.model.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 物流信息值对象
 */
public class LogisticsInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String logisticsCompany;
    private String trackingNumber;
    private List<LogisticsTrack> tracks;

    public LogisticsInfo(String logisticsCompany, String trackingNumber) {
        this.logisticsCompany = logisticsCompany;
        this.trackingNumber = trackingNumber;
        this.tracks = new ArrayList<>();
    }

    public LogisticsInfo(String logisticsCompany, String trackingNumber, List<LogisticsTrack> tracks) {
        this(logisticsCompany, trackingNumber);
        if (tracks != null) {
            this.tracks.addAll(tracks);
        }
    }

    /**
     * 添加物流轨迹
     */
    public void addTrack(LocalDateTime time, String location, String description) {
        this.tracks.add(new LogisticsTrack(time, location, description));
    }

    // Getters
    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public List<LogisticsTrack> getTracks() {
        return Collections.unmodifiableList(tracks);
    }

    @Override
    public String toString() {
        return "LogisticsInfo{" +
                "logisticsCompany='" + logisticsCompany + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", tracks=" + tracks +
                '}';
    }

    /**
     * 物流轨迹
     */
    public static class LogisticsTrack implements Serializable {
        
        private static final long serialVersionUID = 1L;

        private final LocalDateTime time;
        private final String location;
        private final String description;

        public LogisticsTrack(LocalDateTime time, String location, String description) {
            if (time == null) {
                throw new IllegalArgumentException("轨迹时间不能为空");
            }
            if (location == null || location.isEmpty()) {
                throw new IllegalArgumentException("轨迹地点不能为空");
            }
            if (description == null || description.isEmpty()) {
                throw new IllegalArgumentException("轨迹描述不能为空");
            }
            this.time = time;
            this.location = location;
            this.description = description;
        }

        // Getters
        public LocalDateTime getTime() {
            return time;
        }

        public String getLocation() {
            return location;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return "LogisticsTrack{" +
                    "time=" + time +
                    ", location='" + location + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
}