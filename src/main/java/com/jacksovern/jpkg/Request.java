package com.jacksovern.jpkg;

import com.fasterxml.jackson.databind.ObjectMapper;

class Request {
    private Package pack;
    private int timeSinceUpdate;

    Request(Package pack, int timeSinceUpdate) {
        this.pack = pack;
        this.timeSinceUpdate = timeSinceUpdate;
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
