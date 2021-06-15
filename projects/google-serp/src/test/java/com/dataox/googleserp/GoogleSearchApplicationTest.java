package com.dataox.googleserp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * @author Viacheslav_Yakovenko
 * @since 09.06.2021
 */
public class GoogleSearchApplicationTest {
    @Test
    void hibernateSettings() throws FileNotFoundException {
        File configProperties = new File("./config/application.yaml");
        File properties;
        if (configProperties.exists()){
            properties = configProperties;
        } else {
            properties = new File(getClass().getResource("/application.yaml").getFile());
        }

        Scanner scanner = new Scanner(properties);
        Boolean correctDdlAuto = false;
        while (scanner.hasNextLine()) {
            if (scanner.nextLine().contains("ddl-auto: update")) {
                correctDdlAuto = true;
                break;
            }
        }
        assertThat(correctDdlAuto).isTrue();
    }
}
