package com.dataox.loadbalancer;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

class LoadBalancerApplicationTests {

    @Test
    void hibernateSettings() throws FileNotFoundException {
        File properties = new File(getClass().getResource("/application.yaml").getFile());
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
