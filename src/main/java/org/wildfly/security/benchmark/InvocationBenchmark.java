/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.wildfly.security.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.wildfly.security.x500.X500PrincipalUtil;
import org.wildfly.security.x500.X500PrincipalUtilMethodHandles;
import org.wildfly.security.x500.X500PrincipalUtilReflection;

/**
 * Compares the three implementations of {@link X500PrincipalUtil}:
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
@Fork(value = 5, jvmArgsAppend = {/*"-verbose:gc", "-XX:+PrintCompilation", */ "-server", "-Xms2g", "-Xmx8g"})
@Warmup(iterations = 100) // there is still some JIT compilation happening after 100 iterations, but not much; using 1000 iterations would prolong the runtime too much
@Measurement(iterations = 10)
@Threads(4)
public class InvocationBenchmark {

    @Benchmark
    public void originalAsX500Principal(Blackhole blackhole, BenchmarkState state) {
        final int cnt = state.principals.length;
        for (int i = 0; i < cnt ; i++) {
            blackhole.consume(X500PrincipalUtil.asX500Principal(state.principals[i]));
        }
    }

    @Benchmark
    public void reflectedAsX500Principal(Blackhole blackhole, BenchmarkState state) {
        final int cnt = state.principals.length;
        for (int i = 0; i < cnt ; i++) {
            blackhole.consume(X500PrincipalUtilReflection.asX500Principal(state.principals[i]));
        }
    }

    @Benchmark
    public void methodHandlesAsX500Principal(Blackhole blackhole, BenchmarkState state) {
        final int cnt = state.principals.length;
        for (int i = 0; i < cnt ; i++) {
            blackhole.consume(X500PrincipalUtilMethodHandles.asX500Principal(state.principals[i]));
        }
    }

}
