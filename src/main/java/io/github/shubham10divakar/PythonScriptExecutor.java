package io.github.shubham10divakar;

import io.github.shubham10divakar.CustomException.IncorrectFileExtensionException;

import java.io.*;

public class PythonScriptExecutor {
    public String executePythonScript(String scriptPath) {

        String output = null;
        try {
            output = executeCoreLogic(scriptPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println(e);
        }

        if(output==null){
            return "Some Issue Was Encountered, please investigate.";
        }
        return output;
    }

    private String executeCoreLogic(String scriptPath) throws IOException, InterruptedException {
        //String scriptPath = "src/main/resources/pythonscript/nmap_scan.py"; // Adjust the path as needed

        File scriptFile = validateFilePath(scriptPath);

        System.out.println("Using Python script at: " + scriptFile.getAbsolutePath());

        ProcessBuilder processBuilder = new ProcessBuilder("python", scriptFile.getAbsolutePath());
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();
        StringBuilder errorOutput = new StringBuilder();
        //boolean scanSuccess = false;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
//                if (line.contains("Scan completed successfully.")) {
//                    scanSuccess = true;
//                }
                if (line.contains("Error:")) {
                    errorOutput.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading output from Python script", e);
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            String errorMessage = "Failed with exit code " + exitCode + ". Output: " + output.toString();
            if (errorOutput.length() > 0) {
                errorMessage += " Error Output: " + errorOutput.toString();
            }
            throw new RuntimeException(errorMessage);
        }

        return output.toString();  // This should return JSON data from the Python script
    }

    private File validateFilePath(String scriptPath) throws FileNotFoundException {

        if(!scriptPath.endsWith(".py")){
            throw new IncorrectFileExtensionException("Path is not a valid path for python file, include python(.py) file in the path in case you missed to add: " + scriptPath);
        }

        File scriptFile = new File(scriptPath);
        if (!scriptFile.exists()) {
            throw new FileNotFoundException("Python script not found at path: " + scriptPath);
        }

        return scriptFile;
    }
}
