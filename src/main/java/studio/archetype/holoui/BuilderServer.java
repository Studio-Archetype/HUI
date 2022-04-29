package studio.archetype.holoui;

import com.github.zafarkhaja.semver.Version;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.iki.elonen.NanoHTTPD;
import org.apache.commons.io.FileUtils;
import studio.archetype.holoui.config.HuiSettings;
import studio.archetype.holoui.utils.WebUtils;
import studio.archetype.holoui.utils.ZipUtils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class BuilderServer extends NanoHTTPD implements Runnable {

    private static final String URL = "https://api.github.com/repos/Studio-Archetype/HUI-Builder/releases/latest";
    private static final String BUILT_NAME = "builder_static.zip";

    private final File serverDir, versionFile;

    private String version;

    public BuilderServer(File pluginDir) {
        super(HuiSettings.BUILDER_PORT.value());
        serverDir = new File(pluginDir, "builder");
        versionFile = new File(serverDir, "version");
    }

    public boolean prepareServer() {
        try {
            JsonElement latestManifest = WebUtils.getJson(URL);
            HoloUI.log(Level.INFO, "Preparing Builder server...");
            if(shouldRedownload(latestManifest)) {
                JsonArray assets = latestManifest.getAsJsonObject().getAsJsonArray("assets");
                downloadServer(getZipUrl(assets));
            }
            HoloUI.log(Level.INFO, "Server ready!");
            return true;
        } catch(IOException e) {
            HoloUI.logExceptionStack(true, e, "Failed to setup builder server:");
            return false;
        }
    }

    private String getZipUrl(JsonArray assets) throws IOException {
        for(JsonElement asset : assets) {
            JsonObject entry = asset.getAsJsonObject();
            if(entry.get("name").getAsString().equalsIgnoreCase(BUILT_NAME))
                return entry.get("browser_download_url").getAsString();
        }
        throw new IOException("Invalid release manifest: No server built available!");
    }

    private void prepareFolder() throws IOException {
        if(serverDir.exists()) {
            if(serverDir.isDirectory())
                FileUtils.deleteDirectory(serverDir);
            else
                FileUtils.deleteQuietly(serverDir);
        }
        serverDir.mkdirs();
    }

    private void downloadServer(String contentUrl) throws IOException {
        prepareFolder();
        File archive = new File(serverDir, "server.zip");
        HoloUI.log(Level.INFO, "\tDownloading latest builder...");
        WebUtils.downloadFile(contentUrl, archive);
        HoloUI.log(Level.INFO, "\tExtracting archive...");
        ZipUtils.unzipFile(archive, serverDir);
        HoloUI.log(Level.INFO, "\tRemoving archive...");
        archive.delete();
        FileUtils.writeStringToFile(versionFile, version, "UTF-8");
        HoloUI.log(Level.INFO, "\tDone!");
    }

    private boolean shouldRedownload(JsonElement fetchedMeta) throws IOException {
        Version remote = Version.valueOf(fetchedMeta.getAsJsonObject().get("tag_name").getAsString());
        if(!versionFile.exists() || versionFile.isDirectory()) {
            this.version = remote.toString();
            return true;
        } else {
            Version local = Version.valueOf(FileUtils.readFileToString(versionFile, "UTF-8"));
            if(remote.greaterThan(local)) {
                HoloUI.log(Level.INFO, "Newer version found! [%s -> %s]", local, remote);
                this.version = remote.toString();
                return true;
            }
            HoloUI.log(Level.INFO, "No newer version found. [%s]", local);
            this.version = local.toString();
            return false;
        }
    }

    /*public Response serve(IHTTPSession session) {
        Map<String, String> header = session.getHeaders();
        Map<String, List<String>> parms = session.getParameters();
        String uri = session.getUri();

        for (File homeDir : this.rootDirs) {
            // Make sure we won't die of an exception later
            if (!homeDir.isDirectory()) {
                return getInternalErrorResponse("given path is not a directory (" + homeDir + ").");
            }
        }
        return respond(Collections.unmodifiableMap(header), session, uri);
    }*/

    @Override
    public void run() {

    }
}
