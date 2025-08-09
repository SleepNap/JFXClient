package org.jc.main.core.database;

import java.io.*;

public class PostgresOperator {

    public static boolean startPG(File pgDir) throws IOException {
        if (statusPG(pgDir)) {
            return true;
        }
        Process process = Runtime.getRuntime().exec(new String[]{pgDir.getAbsolutePath() + "\\bin\\pg_ctl.exe", "-D", pgDir.getAbsolutePath() + "\\data", "start"});
        InputStream[] streams = new InputStream[]{process.getInputStream(), process.getErrorStream()};
        String[] keywords = new String[]{"database system is ready"};
        if (checkOutput(streams, keywords)) {
            return true;
        }
        return statusPG(pgDir);
    }

    public static boolean stopPG(File pgDir) throws IOException {
        if (!statusPG(pgDir)) {
            return true;
        }
        Process process = Runtime.getRuntime().exec(new String[]{pgDir.getAbsolutePath() + "\\bin\\pg_ctl.exe", "-D", pgDir.getAbsolutePath() + "\\data", "stop"});
        InputStream[] streams = new InputStream[]{process.getInputStream(), process.getErrorStream()};
        String[] keywords = new String[]{"database system is shut down"};
        if (checkOutput(streams, keywords)) {
            return true;
        }
        return !statusPG(pgDir);
    }

    public static boolean statusPG(File pgDir) throws IOException {
        if (!pgDir.exists()) {
            return false;
        }
        Process process = Runtime.getRuntime().exec(new String[]{pgDir.getAbsolutePath() + "\\bin\\pg_ctl.exe", "-D", pgDir.getAbsolutePath() + "\\data", "status"});
        InputStream[] streams = new InputStream[]{process.getInputStream(), process.getErrorStream()};
        String[] keywords = new String[]{"server is running", "PID"};
        return checkOutput(streams, keywords);
    }

    private static boolean checkOutput(InputStream[] streams, String[] keywords) throws IOException {
        BufferedReader[] readers = new BufferedReader[streams.length];
        for (int i = 0; i < streams.length; i++) {
            readers[i] = new BufferedReader(new InputStreamReader(streams[i]));
        }
        long startTime = System.currentTimeMillis();
        // 依旧和mysql一样，输出竟然在ErrorStream里
        for (BufferedReader reader : readers) {
            while (System.currentTimeMillis() - startTime < 3000) {
                String output = reader.readLine();
                if (output == null) {
                    continue;
                }
                for (String keyword : keywords) {
                    if (output.contains(keyword)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
