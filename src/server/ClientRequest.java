class ClientRequest{
    private Package packageRecieved;
    private Package[] packageList;
    private long lastUpdated;

    public ClientRequest(Package packageRecieved, Package[] packageList, long lastUpdated) {
        this.packageRecieved = packageRecieved;
        this.packageList = packageList;
        this.lastUpdated = lastUpdated;
    }

    public Package getPackage() {
        return packageRecieved;
    }

    public Package[] getPackageList() {
        return packageList;
    }

    public void setTimeSinceUpdate(long time) {
        lastUpdated = time;
    }

    public long timeSinceUpdate() {
        return System.currentTimeMillis() - lastUpdated;
    }

    public static ClientRequest fromBytes(byte[] bytes) {
        // TODO implement this
        return new ClientRequest(new Package(), new Package[0], 0);
    }
}
