package com.example.aplikasiku.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RatePerwaktuRes {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("total")
        @Expose
        private String total;
        @SerializedName("rate")
        @Expose
        private List<Rate> rate = null;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public List<Rate> getRate() {
            return rate;
        }

        public void setRate(List<Rate> rate) {
            this.rate = rate;
        }

        public class Rate {

            @SerializedName("waktu")
            @Expose
            private String waktu;
            @SerializedName("rate")
            @Expose
            private String rate;

            public String getWaktu() {
                return waktu;
            }

            public void setWaktu(String waktu) {
                this.waktu = waktu;
            }

            public String getRate() {
                return rate;
            }

            public void setRate(String rate) {
                this.rate = rate;
            }

        }

    }
}
