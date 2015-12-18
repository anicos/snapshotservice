package pl.anicos.snapshot.fx;

import org.springframework.stereotype.Component;
import pl.anicos.snapshot.exception.PageNotFoundException;
import pl.anicos.snapshot.model.ResultStatus;

import java.io.*;
import java.util.concurrent.TimeUnit;

@Component
public class JavaFxProcessExecutor {

    public String exec(Class clazz, String args) throws IOException,
            InterruptedException {
        ProcessBuilder builder = createProcessBuilder(clazz, args);

        Process process = builder.start();
        BufferedReader reader = getBufferedReader(process);

        String line;

        while ((line = reader.readLine()) != null) {

            if (line.startsWith(ResultStatus.SUCCESS.name())) {
                closeProcess(process, reader);
                return line.substring(ResultStatus.SUCCESS.name().length());
            }

            if (line.startsWith(ResultStatus.FAILURE.name())) {
                closeProcess(process, reader);
                throw new PageNotFoundException();
            }
        }

        if (!process.waitFor(30, TimeUnit.SECONDS)) {
            closeProcess(process, reader);
            throw new PageNotFoundException();
        }
        throw new PageNotFoundException();
    }

    private void closeProcess(Process process, BufferedReader reader) throws IOException {
        reader.close();
        process.destroy();
    }

    private BufferedReader getBufferedReader(Process process) {
        InputStream stdout = process.getInputStream();
        return new BufferedReader(new InputStreamReader(stdout));
    }

    private ProcessBuilder createProcessBuilder(Class klass, String args) {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome +
                File.separator + "bin" +
                File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = klass.getCanonicalName();

        ProcessBuilder builder = new ProcessBuilder(
                javaBin, "-cp", classpath, className, args);

        builder.redirectErrorStream(true);
        return builder;
    }
}