import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

public class GradleBootstrapper {

    public static void main(String[] args) throws Exception {
        File projectRoot = new File(System.getProperty("user.dir"));

        File propsFile = new File(projectRoot, "gradle/wrapper/gradle-wrapper.properties");
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream(propsFile);
        props.load(fis);
        fis.close();

        String rawUrl = props.getProperty("distributionUrl", "");
        String distributionUrl = rawUrl.replace("\\:", ":").replace("\\\\", "/");

        String gradleUserHomeStr = System.getenv("GRADLE_USER_HOME");
        File gradleUserHome = (gradleUserHomeStr != null)
                ? new File(gradleUserHomeStr)
                : new File(System.getProperty("user.home"), ".gradle");

        String distName = getDistName(distributionUrl);
        File distsDir = new File(gradleUserHome, "wrapper/dists");
        File installDir = new File(distsDir, distName);
        File zipFile = new File(distsDir, distName + ".zip");

        File gradleHome = findGradleHome(installDir);

        if (gradleHome == null) {
            System.out.println("Downloading Gradle: " + distributionUrl);
            distsDir.mkdirs();
            downloadFile(distributionUrl, zipFile);
            System.out.println("\nExtracting...");
            installDir.mkdirs();
            unzip(zipFile, installDir);
            gradleHome = findGradleHome(installDir);
            if (gradleHome == null) {
                throw new RuntimeException("Extraction failed: " + installDir);
            }
        } else {
            System.out.println("Gradle found: " + gradleHome.getAbsolutePath());
        }

        String javaExe = System.getProperty("java.home") + "/bin/java";
        File libDir = new File(gradleHome, "lib");
        String launcherJar = findLauncherJar(libDir);
        if (launcherJar == null) {
            throw new RuntimeException("gradle-launcher jar not found: " + libDir);
        }

        List cmd = new ArrayList();
        cmd.add(javaExe);

        String jvmArgs = System.getenv("GRADLE_OPTS");
        if (jvmArgs != null && !jvmArgs.isEmpty()) {
            String[] parts = jvmArgs.trim().split("\\s+");
            for (int i = 0; i < parts.length; i++) {
                if (!parts[i].isEmpty()) cmd.add(parts[i]);
            }
        }

        cmd.add("-Dorg.gradle.appname=gradlew");
        cmd.add("-classpath");
        cmd.add(launcherJar);
        cmd.add("org.gradle.launcher.GradleMain");
        for (int i = 0; i < args.length; i++) {
            cmd.add(args[i]);
        }

        System.out.println("Launching Gradle...");
        String[] cmdArr = new String[cmd.size()];
        for (int i = 0; i < cmd.size(); i++) cmdArr[i] = (String) cmd.get(i);

        ProcessBuilder pb = new ProcessBuilder(cmdArr);
        pb.directory(projectRoot);
        pb.inheritIO();
        pb.environment().put("GRADLE_HOME", gradleHome.getAbsolutePath());
        pb.environment().put("JAVA_HOME", System.getProperty("java.home"));
        System.exit(pb.start().waitFor());
    }

    private static String getDistName(String url) {
        String name = url.substring(url.lastIndexOf('/') + 1);
        return name.endsWith(".zip") ? name.substring(0, name.length() - 4) : name;
    }

    private static File findGradleHome(File installDir) {
        if (!installDir.exists()) return null;
        File[] subdirs = installDir.listFiles();
        if (subdirs == null) return null;
        for (int i = 0; i < subdirs.length; i++) {
            if (subdirs[i].isDirectory() && new File(subdirs[i], "bin/gradle").exists()) {
                return subdirs[i];
            }
        }
        return null;
    }

    private static void downloadFile(String urlStr, File dest) throws Exception {
        dest.getParentFile().mkdirs();
        URL url = new URL(urlStr);
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(600000);
        InputStream in = conn.getInputStream();
        FileOutputStream out = new FileOutputStream(dest);
        byte[] buf = new byte[32768];
        long downloaded = 0;
        int n;
        try {
            while ((n = in.read(buf)) != -1) {
                out.write(buf, 0, n);
                downloaded += n;
                System.out.print("\r  " + (downloaded / 1024 / 1024) + " MB downloaded");
            }
        } finally {
            in.close();
            out.close();
        }
    }

    private static void unzip(File zipFile, File destDir) throws Exception {
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry entry;
        try {
            while ((entry = zis.getNextEntry()) != null) {
                File out = new File(destDir, entry.getName());
                if (entry.isDirectory()) {
                    out.mkdirs();
                } else {
                    out.getParentFile().mkdirs();
                    FileOutputStream fos = new FileOutputStream(out);
                    byte[] buf = new byte[32768];
                    int n;
                    try {
                        while ((n = zis.read(buf)) != -1) fos.write(buf, 0, n);
                    } finally {
                        fos.close();
                    }
                    String name = out.getName();
                    if (name.equals("gradle") || name.endsWith(".sh")) {
                        out.setExecutable(true);
                    }
                }
                zis.closeEntry();
            }
        } finally {
            zis.close();
        }
    }

    private static String findLauncherJar(File libDir) {
        if (!libDir.exists()) return null;
        File[] jars = libDir.listFiles();
        if (jars == null) return null;
        // Find gradle-launcher jar first
        for (int i = 0; i < jars.length; i++) {
            if (jars[i].getName().startsWith("gradle-launcher") && jars[i].getName().endsWith(".jar")) {
                return jars[i].getAbsolutePath();
            }
        }
        // Fallback: all jars
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < jars.length; i++) {
            if (jars[i].getName().endsWith(".jar")) {
                if (sb.length() > 0) sb.append(File.pathSeparatorChar);
                sb.append(jars[i].getAbsolutePath());
            }
        }
        return sb.length() > 0 ? sb.toString() : null;
    }
}
