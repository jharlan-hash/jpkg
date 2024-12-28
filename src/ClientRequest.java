import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

class ClientRequest{
    private Package packageReceived;
    private Package[] packageList = null;
    private long lastUpdated;

    public ClientRequest(Package packageReceived, Package[] packageList, long lastUpdated) {
        this.packageReceived = packageReceived;
        this.packageList = packageList;
        this.lastUpdated = lastUpdated;
    }

    public Package getPackage() {
        return packageReceived;
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

    public byte[] toBytes() throws IOException {
        try (ByteArrayOutputStream bArrayOut = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(bArrayOut)) {

            // Write packageReceived
            writePackage(dataOut, packageReceived);

            // Write packageList
            dataOut.writeInt(packageList != null ? packageList.length : 0);
            if (packageList != null) {
                for (Package pkg : packageList) {
                    writePackage(dataOut, pkg);
                }
            }

            // Write lastUpdated
            dataOut.writeLong(lastUpdated);

            return bArrayOut.toByteArray();
        }
    }

    public static ClientRequest fromBytes(byte[] bytes) throws IOException {
        try (ByteArrayInputStream bArrayIn = new ByteArrayInputStream(bytes);
        DataInputStream dataIn = new DataInputStream(bArrayIn)) {

            // Read packageReceived
            Package packageReceived = readPackage(dataIn);

            // Read packageList
            int listLength = dataIn.readInt();
            Package[] packageList = new Package[listLength];
            for (int i = 0; i < listLength; i++) {
                packageList[i] = readPackage(dataIn);
            }

            // Read lastUpdated
            long lastUpdated = dataIn.readLong();

            return new ClientRequest(packageReceived, packageList, lastUpdated);
        }
    }

    private static void writePackage(DataOutputStream dataOut, Package pkg) throws IOException {
        // Write strings with their length prefix
        writeString(dataOut, pkg.getName());
        writeString(dataOut, pkg.getVersion());
        writeString(dataOut, pkg.getDescription());

        // Write id
        dataOut.writeInt(pkg.getId());

        // Write dependencies
        Package[] dependencies = pkg.getDependencies();
        dataOut.writeInt(dependencies.length);
        for (Package pack : dependencies) {
            writePackage(dataOut, pack);
        }
    }

    private static Package readPackage(DataInputStream dataIn) throws IOException {
        // Read strings
        String name = readString(dataIn);
        String version = readString(dataIn);
        String description = readString(dataIn);

        // Read id
        int id = dataIn.readInt();

        // Read dependencies
        int depsLength = dataIn.readInt();
        Package[] dependencies = new Package[depsLength];
        for (int i = 0; i < depsLength; i++) {
            dependencies[i] = readPackage(dataIn);
        }

        return new Package(name, version, description, dependencies, id);
    }

    private static void writeString(DataOutputStream dataOut, String str) throws IOException {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        dataOut.writeInt(bytes.length);
        dataOut.write(bytes);
    }

    private static String readString(DataInputStream dataIn) throws IOException {
        int length = dataIn.readInt();
        byte[] bytes = new byte[length];
        dataIn.readFully(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
