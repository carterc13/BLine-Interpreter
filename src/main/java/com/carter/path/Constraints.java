package com.carter.path;

import java.util.List;

public class Constraints {
    public double end_translation_tolerance_meters;
    public double end_rotation_tolerance_deg;
    public List<VelocityConstraint> max_velocity_meters_per_sec;
    public List<VelocityConstraint> max_acceleration_meters_per_sec2;
    public List<VelocityConstraint> max_velocity_deg_per_sec;
    public List<VelocityConstraint> max_acceleration_deg_per_sec2;
}