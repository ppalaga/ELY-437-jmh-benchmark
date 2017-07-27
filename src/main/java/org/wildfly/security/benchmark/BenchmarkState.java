package org.wildfly.security.benchmark;

import java.io.IOException;
import java.security.Principal;
import java.util.Random;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

/**
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
@State(Scope.Thread)
public class BenchmarkState {
    Principal[] principals;

    private static final int PRINCIPALS_COUNT = 256;

    @Setup(Level.Iteration)
    public void setup() {
        try {
            Random rnd = new Random();
            principals = new Principal[PRINCIPALS_COUNT];
            for (int i = 0; i < PRINCIPALS_COUNT; i++) {
                principals[i] = new sun.security.x509.X500Name("cn="+ rnd.nextInt(Integer.MAX_VALUE));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}