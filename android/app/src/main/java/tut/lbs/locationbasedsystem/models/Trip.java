package tut.lbs.locationbasedsystem.models;

import java.io.Serializable;

public class Trip implements Serializable{
    public long id;
    public String reference;
    public double from_latitude;
    public double from_longitude;
    public double to_latitude;
    public double to_longitude;
    public String status;
    public long user_id;
    public String created_at;
    public String updated_at;
    public User user;

}
