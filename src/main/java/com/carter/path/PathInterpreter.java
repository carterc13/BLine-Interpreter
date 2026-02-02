package com.carter.path;

import java.io.File;
import java.nio.file.Files;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PathInterpreter {

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Directory containing path JSON files
        File pathsDir = new File("paths");

        if (!pathsDir.exists() || !pathsDir.isDirectory()) {
            System.err.println("Could not find 'paths' directory. Make sure it exists at project root.");
            return;
        }

        // Load all .json files in /paths/
        Files.list(pathsDir.toPath()).filter(p -> p.toString().endsWith(".json")).forEach(jsonPath -> {
            try {
                System.out.println("\n=== Loading: " + jsonPath.getFileName() + " ===");

                PathFile pathFile = mapper.readValue(jsonPath.toFile(), PathFile.class);

                interpret(pathFile);

            } catch (Exception e) {
                System.err.println("Error reading " + jsonPath + ": " + e.getMessage());
            }
        });
    }

    private static void interpret(PathFile file) {
        StringBuilder sb = new StringBuilder();
        Constraints c = file.constraints;
        sb.append("CONSTRAINTS|");
        sb.append(c.end_translation_tolerance_meters).append(",");
        sb.append(c.end_rotation_tolerance_deg).append("::");
        sb.append(packConstraintList(c.max_velocity_meters_per_sec)).append("::");
        sb.append(packConstraintList(c.max_acceleration_meters_per_sec2)).append("::");
        sb.append(packConstraintList(c.max_velocity_deg_per_sec)).append("::");
        sb.append(packConstraintList(c.max_acceleration_deg_per_sec2));
        sb.append("##");
        sb.append("ELEMENTS|");
        for (PathElement element : file.path_elements) {
            if (element instanceof Translation t) {
                sb.append("translation|").append(t.x_meters).append(",").append(t.y_meters).append(",")
                        .append(t.intermediate_handoff_radius_meters);
            } else if (element instanceof EventTrigger e) {
                sb.append("event_trigger|").append(e.t_ratio).append(",").append(e.lib_key);
            } else if (element instanceof Rotation r) {
                sb.append("rotation|").append(r.rotation_radians).append(",").append(r.t_ratio).append(",")
                        .append(r.profiled_rotation);
            } else if (element instanceof Waypoint w) {
                sb.append("waypoint|").append(w.translation_target.x_meters).append(",")
                        .append(w.translation_target.y_meters).append(",")
                        .append(w.translation_target.intermediate_handoff_radius_meters).append(",")
                        .append(w.rotation_target.rotation_radians).append(",")
                        .append(w.rotation_target.profiled_rotation);
            }
            sb.append(";;");
        }
        for (int i = 0; i < 5; i++) {
            System.out.println();
        }
        System.out.println(sb.toString());
        for (int i = 0; i < 5; i++) {
            System.out.println();
        }
    }

    private static String packConstraintList(java.util.List<VelocityConstraint> list) {
        if (list == null || list.isEmpty())
            return "EMPTY";
        StringBuilder sb = new StringBuilder();
        for (VelocityConstraint vc : list) {
            sb.append(vc.value).append("/").append(vc.start_ordinal).append("/").append(vc.end_ordinal).append(";");
        }
        return sb.toString();
    }
}
