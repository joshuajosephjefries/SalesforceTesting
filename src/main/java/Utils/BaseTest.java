package Utils;

import com.aventstack.chaintest.plugins.ChainTestListener;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class BaseTest {
    protected WebDriver driver;

    @AfterSuite
    public void afterSuiteTasks(){
        archiveReport();
        openLatestChainTestReport();
    }

    public static void archiveReport() {
        String timestamp = new SimpleDateFormat("E, dd MMM yyyy HH_mm_ss z").format(new Date());
        Path sourceDir = Paths.get("target", "chaintest");
        Path targetDir = Paths.get("reports", "chaintest-report-" + timestamp);

        try {
            Files.walk(sourceDir)
                    .forEach(source -> {
                        Path destination = targetDir.resolve(sourceDir.relativize(source));
                        try {
                            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            System.err.println("Failed to copy file: " + source + " to " + destination);
                            e.printStackTrace();
                        }
                    });
            System.out.println("Report archived to: " + targetDir.toString());
        } catch (IOException e) {
            System.err.println("Failed to archive report.");
            e.printStackTrace();
        }
    }

    public void openLatestChainTestReport() {
        try {
            String reportDirPath = "reports";
            File reportDir = new File(reportDirPath);

            // Filter folders that start with "chaintest-report-"
            File[] matchingFolders = reportDir.listFiles((dir, name) -> name.startsWith("chaintest-report-"));

            if (matchingFolders == null || matchingFolders.length == 0) {
                System.out.println("❌ No chaintest-report folders found.");
                return;
            }

            // Sort to find the latest by last modified time
            File latestReportFolder = Arrays.stream(matchingFolders)
                    .max(Comparator.comparingLong(File::lastModified))
                    .orElseThrow();

            // Path to index.html inside the latest report folder
            String indexPath = new File(latestReportFolder, "index.html").getAbsolutePath();

            // Open in Chrome
            String chromePath = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe"; // Adjust path as needed
            Runtime.getRuntime().exec(new String[]{chromePath, indexPath});

            System.out.println("✅ Opened report: " + indexPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeClass
    public void setUp() {
        driver = DriverSetup.SetDriver();
    }

//    @AfterClass
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }

    public void embedScreenshot() {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        ChainTestListener.embed(screenshot, "image/png");
    }
}
