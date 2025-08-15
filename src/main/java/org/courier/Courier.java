package org.courier;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.concurrent.ThreadLocalRandom;

@Data
@AllArgsConstructor
public class Courier {
    private String login;
    private String password;
    private String firstName;

    public Courier() {
    }

    public static Courier random() {
        int suffix = ThreadLocalRandom.current().nextInt(100, 100_000);
        return new Courier("Kseniya" + suffix, "5588pass", "Gunka");
    }
}
