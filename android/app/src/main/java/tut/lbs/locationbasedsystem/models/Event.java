package tut.lbs.locationbasedsystem.models;

import java.io.Serializable;

public class Event implements Serializable{
    public long id;
    public double current_latitude;
    public double current_longitude;
    public long trip_id;
    public Trip trip;
    public String created_at;
    public String updated_at;
}
