package com.jacksovern.jpkg;

import java.io.Serializable;
import com.fasterxml.jackson.databind.ObjectMapper;

class Package implements Serializable {
    private String name;
    private String version;
    private String description;
    private Package[] dependencies;
    private int id;

    public Package(String name, String version, String description, Package[] dependencies, int id) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.dependencies = dependencies;
        this.id = id;
    }

    public Package(String name, String version, String description, int id) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.dependencies = new Package[0];
        this.id = id;
    }

    public Package() {
        this.name = "";
        this.version = "";
        this.description = "";
        this.dependencies = new Package[0];
        this.id = -1;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public Package[] getDependencies() {
        return dependencies;
    }

    public int getId() {
        return id;
    }

    public String toJSON() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            String json = mapper.writeValueAsString(this);
            return json;
        } catch (Exception e) {
            return "";
        }
    }

    public static Package fromJSON(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Package pack = mapper.readValue(json, Package.class);
            return pack;
        } catch (Exception e) {
            return new Package();
        }
    }

    public byte[] toBytes() {
        return toJSON().getBytes();
    }
}

