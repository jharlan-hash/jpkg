class Package {
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
}