package x.common.entity;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Author: cxx
 * Date: 2020-07-17
 * GitHub: https://github.com/ccolorcat
 */
public class Result<T> {
    public static final int STATUS_OK = 1;

    //    @JSONField(name = "status")
//    @JsonProperty("status")
    @SerializedName("status")
    private int status;
    //    @JSONField(name = "msg")
//    @JsonProperty("msg")
    @SerializedName("msg")
    private String msg;
    //    @JSONField(name = "data")
//    @JsonProperty("data")
    @SerializedName("data")
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result<?> result = (Result<?>) o;
        return status == result.status &&
                Objects.equals(msg, result.msg) &&
                Objects.equals(data, result.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, msg, data);
    }

    @NonNull
    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
